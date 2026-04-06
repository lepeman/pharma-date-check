package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import com.lepeman.pharmadatecheck.ui.PharmaBottomAppBar
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme
import com.lepeman.pharmadatecheck.ui.viewmodels.HistorialViewModel
import java.time.LocalDateTime

/**
 * Contenido stateless de la pantalla de historial de sesiones.
 *
 * Decide qué vista mostrar según el estado de [detalleState]:
 * - Si hay una sesión seleccionada ([HistorialViewModel.DetalleSesionState.Visible]),
 *   muestra [PantallaDetalleSesion] con el detalle de productos revisados.
 * - En cualquier otro estado, muestra [PantallaListaSesiones] con el listado
 *   completo de sesiones registradas.
 *
 * @param historialSelected Indica si esta pantalla es la ruta activa en la
 * barra de navegación inferior.
 * @param detalleState Estado actual del detalle de sesión seleccionada.
 * @param uiState Estado actual de la lista de sesiones.
 * @param filtroActivo Clasificación activa para filtrar los productos en el
 * detalle de sesión, o null si no hay filtro aplicado.
 * @param navigateToScan Acción para navegar a la pantalla de escaneo.
 * @param navigateToHistorial Acción para navegar a esta misma pantalla.
 * @param navigateToCanje Acción para navegar a la pantalla de canjes.
 * @param navigateToConfig Acción para navegar a la pantalla de configuración.
 * @param onVolver Callback invocado al volver desde el detalle a la lista.
 * @param onSeleccionarSesion Callback invocado al tocar una tarjeta de sesión.
 * @param onEliminarSesion Callback invocado al solicitar eliminar una sesión.
 */
@Composable
fun HistorialScreenContent(
    historialSelected: Boolean,
    detalleState: HistorialViewModel.DetalleSesionState,
    uiState: HistorialViewModel.UiState,
    filtroActivo: String?,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit,
    onVolver: () -> Unit,
    onSeleccionarSesion: (SesionRevision) -> Unit,
    onEliminarSesion: (SesionRevision) -> Unit
) {
    Scaffold(
        bottomBar = {
            PharmaBottomAppBar(
                historialSelected   = historialSelected,
                navigateToScan      = navigateToScan,
                navigateToHistorial = navigateToHistorial,
                navigateToCanje     = navigateToCanje,
                navigateToConfig    = navigateToConfig
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorFondo)
                .padding(innerPadding)
        ) {
            if (detalleState is HistorialViewModel.DetalleSesionState.Visible) {
                // Vista de detalle de la sesión seleccionada
                PantallaDetalleSesion(
                    sesion       = detalleState.sesion,
                    productos    = detalleState.productos,
                    filtroActivo = filtroActivo,
                    // TODO: conectar con HistorialViewModel.setFiltroClasificacion
                    onFiltroChange = {},
                    onVolver     = onVolver
                )
            } else {
                // Vista de lista de sesiones registradas
                PantallaListaSesiones(
                    uiState             = uiState,
                    onSeleccionarSesion = onSeleccionarSesion,
                    onEliminarSesion    = onEliminarSesion
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistorialScreenContentPreview() {
    val sesion = SesionRevision(
        id              = 9,
        auxiliarId      = 1,
        fechaInicio     = LocalDateTime.now(),
        fechaTermino    = null,
        totalVigentes   = 10,
        totalCanjeables = 5,
        totalVencidos   = 2
    )
    val listaSesionConNombre = listOf(
        HistorialViewModel.SesionConNombre(sesion, "Luis Ortega")
    )
    PharmaDateCheckTheme {
        HistorialScreenContent(
            historialSelected   = true,
            detalleState        = HistorialViewModel.DetalleSesionState.Cargando,
            uiState             = HistorialViewModel.UiState.ConDatos(listaSesionConNombre),
            filtroActivo        = null,
            navigateToScan      = {},
            navigateToHistorial = {},
            navigateToCanje     = {},
            navigateToConfig    = {},
            onVolver            = {},
            onSeleccionarSesion = {},
            onEliminarSesion    = {}
        )
    }
}