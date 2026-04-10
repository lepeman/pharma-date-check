package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import com.lepeman.pharmadatecheck.domain.Clasificacion
import com.lepeman.pharmadatecheck.domain.ResultadoClasificacion
import com.lepeman.pharmadatecheck.ui.PharmaBottomAppBar
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel.ScanUiState
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Contenido stateless de la pantalla de escaneo.
 *
 * Gestiona tres diálogos modales locales:
 * - [DialogInicioSesion]: visible cuando [sesionActivaId] es null.
 * - [DialogFechaVencimiento]: visible cuando [ean13Pendiente] tiene valor,
 *   permitiendo al operador confirmar la fecha antes de clasificar.
 *
 * El flujo completo de operación es:
 * 1. Autenticación por RUT → [onIniciarSesion].
 * 2. Escaneo HID o manual → [onProcesarEAN13Manual].
 * 3. Confirmación de fecha → [onConfirmarFecha] → clasificación y registro.
 * 4. Cierre de sesión → [onCerrarSesion].
 *
 * @param auxiliarSesion Nombre del auxiliar autenticado, o "Invitado".
 * @param scanSelected Indica si esta pantalla es la ruta activa.
 * @param scanUiState Estado actual del clasificador.
 * @param sesionUiState Estado actual de la sesión.
 * @param sesionActivaId Id de la sesión activa, o null si no hay sesión.
 * @param ean13Pendiente EAN-13 esperando confirmación de fecha, o null.
 * @param totalVigentes Contador acumulado de productos VIGENTES.
 * @param totalCanjeables Contador acumulado de productos CANJEABLES.
 * @param totalVencidos Contador acumulado de productos VENCIDOS.
 * @param onIniciarSesion Callback al confirmar el RUT en el diálogo de inicio.
 * @param onCerrarSesion Callback al pulsar "Cerrar sesión".
 * @param onProcesarEAN13Manual Callback al confirmar un EAN-13 manual.
 * @param onConfirmarFecha Callback al confirmar la fecha de vencimiento.
 * @param onResetearEstado Callback al pulsar "Nuevo escaneo".
 * @param navigateToScan Acción para navegar a esta pantalla.
 * @param navigateToHistorial Acción para navegar al historial.
 * @param navigateToCanje Acción para navegar a canjes.
 * @param navigateToConfig Acción para navegar a configuración.
 */
@Composable
fun ScanScreenContent(
    auxiliarSesion: String,
    scanSelected: Boolean,
    scanUiState: ScanUiState,
    sesionUiState: ScanViewModel.SesionUiState,
    sesionActivaId: Int?,
    ean13Pendiente: String?,
    totalVigentes: Int,
    totalCanjeables: Int,
    totalVencidos: Int,
    onIniciarSesion: (String) -> Unit,
    onCerrarSesion: () -> Unit,
    onProcesarEAN13Manual: (String) -> Unit,
    onConfirmarFecha: (String, java.time.LocalDate) -> Unit,
    onResetearEstado: () -> Unit,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit
) {
    val errorSesion = (sesionUiState as? ScanViewModel.SesionUiState.Error)?.mensaje

    var inputManual          by remember { mutableStateOf("") }
    var mostrarDialogoSesion by remember { mutableStateOf(sesionActivaId == null) }
    var rutAuxiliarInput     by remember { mutableStateOf("") }

    LaunchedEffect(sesionActivaId) {
        mostrarDialogoSesion = sesionActivaId == null
    }

    // Diálogo de autenticación por RUT
    if (mostrarDialogoSesion) {
        DialogInicioSesion(
            operadorInput    = rutAuxiliarInput,
            onOperadorChange = { newText ->
                if (newText.length <= 9 &&
                    newText.all { it.isDigit() || it.uppercaseChar() == 'K' }) {
                    rutAuxiliarInput = newText.uppercase()
                }
            },
            onConfirmar = {
                if (rutAuxiliarInput.isNotBlank()) {
                    onIniciarSesion(rutAuxiliarInput.trim())
                    rutAuxiliarInput = ""
                }
            },
            errorMessage = errorSesion
        )
    }

    // Diálogo de confirmación de fecha de vencimiento
    ean13Pendiente?.let { ean13 ->
        DialogFechaVencimiento(
            onConfirmar = { fecha -> onConfirmarFecha(ean13, fecha) },
            onCancelar  = onResetearEstado
        )
    }

    Scaffold(
        bottomBar = {
            PharmaBottomAppBar(
                scanSelected        = scanSelected,
                navigateToScan      = navigateToScan,
                navigateToHistorial = navigateToHistorial,
                navigateToCanje     = navigateToCanje,
                navigateToConfig    = navigateToConfig
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorFondo)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            ContadoresSesion(
                nombreOperador = auxiliarSesion,
                vigentes       = totalVigentes,
                canjeables     = totalCanjeables,
                vencidos       = totalVencidos,
                onCerrarSesion = onCerrarSesion
            )

            ZonaEscaneo(
                inputManual       = inputManual,
                onInputChange     = { inputManual = it },
                onConfirmarManual = {
                    onProcesarEAN13Manual(inputManual.trim())
                    inputManual = ""
                }
            )

            when (val state = scanUiState) {
                is ScanUiState.Idle -> MensajeEspera()
                is ScanUiState.Cargando -> {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ColorVigente)
                    }
                }
                is ScanUiState.ProductoNoEncontrado -> {
                    TarjetaError(stringResource(R.string.producto_no_encontrado))
                }
                is ScanUiState.Error -> {
                    Text(
                        text     = state.mensaje,
                        color    = ColorTexto,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is ScanUiState.Resultado -> {
                    TarjetaResultado(
                        resultado      = state.resultado,
                        onNuevoEscaneo = onResetearEstado
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanScreenContentPreview() {

    val activa = ScanViewModel.SesionUiState.Activa("Luis Ortega")

    PharmaDateCheckTheme {
        ScanScreenContent(
            auxiliarSesion        = "Luis Andrés Ortega Lepe",
            scanSelected          = true,
            scanUiState           = ScanUiState.Idle,
            sesionUiState         = activa,
            sesionActivaId        = 1,
            ean13Pendiente        = null,
            totalVigentes         = 12,
            totalCanjeables       = 3,
            totalVencidos         = 1,
            onIniciarSesion       = {},
            onCerrarSesion        = {},
            onProcesarEAN13Manual = {},
            onConfirmarFecha      = { _, _ -> },
            onResetearEstado      = {},
            navigateToScan        = {},
            navigateToHistorial   = {},
            navigateToCanje       = {},
            navigateToConfig      = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScanScreenContentResultadoPreview() {

    val clasificacion = Clasificacion.VIGENTE

    val producto = Producto(
        codigoEAN13 = "8904103326574",
        nombre = "OTOC 8 MG BUC. DISPER. COM. 10",
        laboratorioId = 1190
    )

    val uiState = ScanUiState.Resultado(
        resultado = ResultadoClasificacion(
            producto = producto,
            nombreLaboratorio = "SEVEN PHARMA",
            fechaVencimiento = LocalDate.of(2026, 5, 1),
            clasificacion = clasificacion,
            diasRestantes = 10,
            fechaLimiteCanje = null
        )
    )

    PharmaDateCheckTheme {
        ScanScreenContent(
            auxiliarSesion        = "Luis Andrés Ortega Lepe",
            scanSelected          = true,
            scanUiState           = uiState,
            sesionUiState         = ScanViewModel.SesionUiState.Activa("Luis Ortega"),
            sesionActivaId        = 1,
            ean13Pendiente        = null,
            totalVigentes         = 12,
            totalCanjeables       = 3,
            totalVencidos         = 1,
            onIniciarSesion       = {},
            onCerrarSesion        = {},
            onProcesarEAN13Manual = {},
            onConfirmarFecha      = { _, _ -> },
            onResetearEstado      = {},
            navigateToScan        = {},
            navigateToHistorial   = {},
            navigateToCanje       = {},
            navigateToConfig      = {}
        )
    }
}