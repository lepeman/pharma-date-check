package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavigation
import com.lepeman.pharmadatecheck.ui.viewmodels.AppViewModelProvider
import com.lepeman.pharmadatecheck.ui.viewmodels.HistorialViewModel

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
    val historialSelected = rutaActual?.hierarchy?.any { it.route == HistorialDestination.route } == true

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

    HistorialScreenContent(
        historialSelected = historialSelected,
        uiState = uiState,
        detalleState = detalleState,
        filtroActivo = filtroActivo,
        navigateToScan = navigateToScan,
        navigateToHistorial = navigateToHistorial,
        navigateToCanje = navigateToCanje,
        navigateToConfig = navigateToConfig,
        onVolver = viewModel::cerrarDetalle,
        onSeleccionarSesion = viewModel::seleccionarSesion,
        onEliminarSesion = viewModel::solicitarEliminar
    )
}