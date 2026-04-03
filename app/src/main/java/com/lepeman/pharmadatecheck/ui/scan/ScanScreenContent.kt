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
import com.lepeman.pharmadatecheck.ui.PharmaBottomAppBar
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel.ScanUiState

/**
 * Versión sin estado de [ScanScreen] para facilitar las pruebas y el renderizado de Previews.
 */
@Composable
fun ScanScreenContent(
    auxiliarSesion: String,
    scanSelected: Boolean,
    scanUiState: ScanUiState,
    sesionUiState: ScanViewModel.SesionUiState,
    sesionActivaId: Int?,
    totalVigentes: Int,
    totalCanjeables: Int,
    totalVencidos: Int,
    onIniciarSesion: (String) -> Unit,
    onCerrarSesion: () -> Unit,
    onProcesarEAN13Manual: (String) -> Unit,
    onResetearEstado: () -> Unit,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit
) {
    val errorSesion = (sesionUiState as? ScanViewModel.SesionUiState.Error)?.mensaje

    var inputManual by remember { mutableStateOf("") }
    // Se mantienen estas variables de estado aunque no se usen explícitamente en el cuerpo
    // proporcionado, para preservar la lógica original del desarrollador.
    @Suppress("UNUSED_VARIABLE")
    var mostrarDialogoFecha by remember { mutableStateOf(false) }
    @Suppress("UNUSED_VARIABLE")
    var ean13Pendiente by remember { mutableStateOf(mutableStateOf(sesionActivaId == null)) }

    var mostrarDialogoSesion by remember { mutableStateOf(sesionActivaId == null) }
    var rutAuxiliarInput by remember { mutableStateOf("") }

    LaunchedEffect(sesionActivaId) {
        mostrarDialogoSesion = sesionActivaId == null
    }

    if (mostrarDialogoSesion) {
        DialogInicioSesion(
            operadorInput = rutAuxiliarInput,
            onOperadorChange = { newText ->
                if (newText.length <= 9 && newText.all { it.isDigit() || it.uppercaseChar() == 'K' }) {
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

    Scaffold(
        bottomBar = {
            PharmaBottomAppBar(
                scanSelected = scanSelected,
                navigateToScan = navigateToScan,
                navigateToHistorial = navigateToHistorial,
                navigateToCanje = navigateToCanje,
                navigateToConfig = navigateToConfig
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
                vigentes = totalVigentes,
                canjeables = totalCanjeables,
                vencidos = totalVencidos,
                onCerrarSesion = onCerrarSesion
            )

            ZonaEscaneo(
                inputManual = inputManual,
                onInputChange = { inputManual = it },
                onConfirmarManual = {
                    onProcesarEAN13Manual(inputManual.trim())
                    inputManual = ""
                }
            )

            when (val state = scanUiState) {
                is ScanUiState.Idle -> {
                    MensajeEspera()
                }
                is ScanUiState.Cargando -> {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ColorTexto)
                    }
                }
                is ScanUiState.ProductoNoEncontrado -> {
                    TarjetaError(stringResource(R.string.producto_no_encontrado))
                }
                is ScanUiState.Error -> {
                    Text(text = state.mensaje, color = ColorTexto, modifier = Modifier.padding(16.dp))
                }
                is ScanUiState.Resultado -> {
                    TarjetaResultado(
                        resultado = state.resultado,
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
    PharmaDateCheckTheme {
        // Usamos la versión sin estado para el Preview, evitando la inicialización del ViewModel
        // que causaba fallos al intentar acceder al Application context en tiempo de diseño.
        ScanScreenContent(
            auxiliarSesion = "Luis Andrés Ortega Lepe",
            scanSelected = true,
            scanUiState = ScanUiState.Idle,
            sesionUiState = ScanViewModel.SesionUiState.Activa("Luis Ortega"),
            sesionActivaId = 1,
            totalVigentes = 12,
            totalCanjeables = 3,
            totalVencidos = 1,
            onIniciarSesion = {},
            onCerrarSesion = {},
            onProcesarEAN13Manual = {},
            onResetearEstado = {},
            navigateToScan = {},
            navigateToHistorial = {},
            navigateToCanje = {},
            navigateToConfig = {}
        )
    }
}