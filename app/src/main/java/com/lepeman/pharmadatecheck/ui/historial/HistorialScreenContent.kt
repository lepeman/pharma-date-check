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
import java.time.LocalDate
import java.time.LocalDateTime

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
                historialSelected = historialSelected,
                navigateToScan = navigateToScan,
                navigateToHistorial = navigateToHistorial,
                navigateToCanje = navigateToCanje,
                navigateToConfig = navigateToConfig
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorFondo)
                .padding(innerPadding)
        ) {
            // Si hay una sesión seleccionada mostramos el detalle
            if (detalleState is HistorialViewModel.DetalleSesionState.Visible) {
                PantallaDetalleSesion(
                    sesion = detalleState.sesion,
                    productos = detalleState.productos,
                    filtroActivo = filtroActivo,
                    onFiltroChange = { /** Por resolver*/ },
                    onVolver = onVolver
                )
            } else {
                // Lista de sesiones
                PantallaListaSesiones(
                    uiState = uiState,
                    onSeleccionarSesion = onSeleccionarSesion,
                    onEliminarSesion = onEliminarSesion
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistorialScreenContentPreview() {

    val sesion = SesionRevision(
        id = 9,
        auxiliarId = 1,
        fechaInicio = LocalDateTime.now(),
        fechaTermino = null,
        totalVigentes = 10,
        totalCanjeables = 5,
        totalVencidos = 2
    )

    val listaSesionConNombre = listOf(HistorialViewModel.SesionConNombre(sesion, "Luis Ortega"))

    PharmaDateCheckTheme {
        HistorialScreenContent(
            historialSelected = true,
            detalleState = HistorialViewModel.DetalleSesionState.Cargando,
            uiState = HistorialViewModel.UiState.ConDatos(
                sesiones = listaSesionConNombre
            ),
            filtroActivo = null,
            navigateToScan = {},
            navigateToHistorial = {},
            navigateToCanje = {},
            navigateToConfig = {},
            onVolver = {},
            onSeleccionarSesion = {},
            onEliminarSesion = {}
        )
    }
}