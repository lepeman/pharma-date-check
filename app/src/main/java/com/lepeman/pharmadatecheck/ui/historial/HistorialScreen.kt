package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lepeman.pharmadatecheck.ui.viewmodels.HistorialViewModel
import com.lepeman.pharmadatecheck.PharmaTopAppBar
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavigation
import com.lepeman.pharmadatecheck.ui.scan.ScanDestination
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.viewmodels.AppViewModelProvider
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel

object HistorialDestination : PharmaNavigation {
    override val route = "historial"
    override val titleRes = R.string.historial
}

@Composable
fun HistorialScreen(
    navController: NavHostController,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit,
    viewModel: HistorialViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination

    val uiState by viewModel.uiState.collectAsState()
    val detalleState by viewModel.detalleState.collectAsState()
    val filtroActivo by viewModel.filtroClasificacion.collectAsState()
    val sesionAEliminar by viewModel.sesionAEliminar.collectAsState()

    sesionAEliminar?.let { sesion ->
        DialogConfirmarEliminarSesion(
            operador = sesion.auxiliarId.toString(),
            onConfirmar = viewModel::confirmarEliminar,
            onCancelar = viewModel::cancelarEliminar
        )
    }

    Scaffold(
        bottomBar = {
            PharmaTopAppBar(
                historialSelected = rutaActual?.hierarchy?.any { it.route == HistorialDestination.route } == true,
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
                val detalle = detalleState as HistorialViewModel.DetalleSesionState.Visible
                PantallaDetalleSesion(
                    sesion = detalle.sesion,
                    productos = detalle.productos,
                    filtroActivo = filtroActivo,
                    onFiltroChange = { /** Por resolver*/ },
                    onVolver = viewModel::cerrarDetalle
                )
            } else {
                // Lista de sesiones
                PantallaListaSesiones(
                    uiState = uiState,
                    onSeleccionarSesion = {},
                    onEliminarSesion = {}
                )
            }
        }
    }
}