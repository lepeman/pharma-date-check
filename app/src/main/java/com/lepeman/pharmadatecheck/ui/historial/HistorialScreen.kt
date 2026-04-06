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

/** Destino de navegación para la pantalla de historial de sesiones. */
object HistorialDestination : PharmaNavigation {
    override val route = "historial"
    override val titleRes = R.string.historial
}

/**
 * Pantalla de historial de sesiones de revisión.
 *
 * Actúa como contenedor de estado (stateful), recopilando los valores
 * expuestos por [HistorialViewModel] y delegando el renderizado a
 * [HistorialScreenContent], que es un composable sin estado (stateless).
 *
 * Gestiona el diálogo de confirmación de eliminación [DialogConfirmarEliminarSesion],
 * que se muestra cuando [HistorialViewModel.sesionAEliminar] tiene un valor distinto
 * de null.
 *
 * @param navController Controlador de navegación para determinar la ruta activa
 * en la barra de navegación inferior.
 * @param navigateToScan Acción para navegar a la pantalla de escaneo.
 * @param navigateToHistorial Acción para navegar a esta misma pantalla.
 * @param navigateToCanje Acción para navegar a la pantalla de canjes.
 * @param navigateToConfig Acción para navegar a la pantalla de configuración.
 * @param viewModel ViewModel que gestiona el estado de la pantalla.
 */
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
    val rutaActual        = navBackStackEntry?.destination
    val historialSelected = rutaActual?.hierarchy?.any { it.route == HistorialDestination.route } == true

    val uiState        by viewModel.uiState.collectAsState()
    val detalleState   by viewModel.detalleState.collectAsState()
    val filtroActivo   by viewModel.filtroClasificacion.collectAsState()
    val sesionAEliminar by viewModel.sesionAEliminar.collectAsState()

    // Diálogo de confirmación de eliminación de sesión
    sesionAEliminar?.let { sesion ->
        DialogConfirmarEliminarSesion(
            operador   = sesion.auxiliarId.toString(),
            onConfirmar = viewModel::confirmarEliminar,
            onCancelar  = viewModel::cancelarEliminar
        )
    }

    HistorialScreenContent(
        historialSelected   = historialSelected,
        uiState             = uiState,
        detalleState        = detalleState,
        filtroActivo        = filtroActivo,
        navigateToScan      = navigateToScan,
        navigateToHistorial = navigateToHistorial,
        navigateToCanje     = navigateToCanje,
        navigateToConfig    = navigateToConfig,
        onVolver            = viewModel::cerrarDetalle,
        onSeleccionarSesion = viewModel::seleccionarSesion,
        onEliminarSesion    = viewModel::solicitarEliminar
    )
}