package com.lepeman.pharmadatecheck.ui.scan

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
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel

/** Destino de navegación para la pantalla de escaneo. */
object ScanDestination : PharmaNavigation {
    override val route = "scan"
    override val titleRes = R.string.scan_screen
}

/**
 * Pantalla de escaneo de productos.
 *
 * Actúa como contenedor de estado (stateful), recopilando los valores
 * expuestos por [ScanViewModel] y delegando el renderizado a
 * [ScanScreenContent], que es un composable sin estado (stateless).
 *
 * Resuelve el nombre del auxiliar a partir del estado de la sesión activa:
 * si hay una sesión iniciada expone el nombre real del auxiliar autenticado,
 * en caso contrario muestra "Invitado" como valor por defecto.
 *
 * @param navController Controlador de navegación para determinar la ruta
 * activa en la barra de navegación inferior.
 * @param navigateToScan Acción para navegar a esta misma pantalla.
 * @param navigateToHistorial Acción para navegar a la pantalla de historial.
 * @param navigateToCanje Acción para navegar a la pantalla de canjes.
 * @param navigateToConfig Acción para navegar a la pantalla de configuración.
 * @param viewModel ViewModel que gestiona el estado de la pantalla.
 */
@Composable
fun ScanScreen(
    navController: NavHostController,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit,
    viewModel: ScanViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual   = navBackStackEntry?.destination
    val scanSelected = rutaActual?.hierarchy?.any { it.route == ScanDestination.route } == true

    val scanUiState    by viewModel.scanUiState.collectAsState()
    val sesionUiState  by viewModel.sesionUiState.collectAsState()
    val sesionActivaId by viewModel.sesionActivaId.collectAsState()
    val ean13Pendiente by viewModel.ean13Pendiente.collectAsState()

    // Resuelve el nombre del auxiliar desde el estado de sesión activa
    val nombreAuxiliar = when (val state = sesionUiState) {
        is ScanViewModel.SesionUiState.Activa -> state.nombreAuxiliar
        else -> "Invitado"
    }

    val totalVigentes   by viewModel.totalVigentes.collectAsState()
    val totalCanjeables by viewModel.totalCanjeables.collectAsState()
    val totalVencidos   by viewModel.totalVencidos.collectAsState()

    ScanScreenContent(
        auxiliarSesion      = nombreAuxiliar,
        scanSelected        = scanSelected,
        scanUiState         = scanUiState,
        sesionUiState       = sesionUiState,
        sesionActivaId      = sesionActivaId,
        ean13Pendiente      = ean13Pendiente,
        totalVigentes       = totalVigentes,
        totalCanjeables     = totalCanjeables,
        totalVencidos       = totalVencidos,
        onIniciarSesion     = { viewModel.iniciarSesion(it) },
        onCerrarSesion      = { viewModel.cerrarSesion() },
        onProcesarEAN13Manual = { viewModel.procesarEAN13Manual(it) },
        onConfirmarFecha    = { ean13, fecha -> viewModel.confirmarFechaYClasificar(ean13, fecha) },
        onResetearEstado    = { viewModel.resetearEstado() },
        navigateToScan      = navigateToScan,
        navigateToHistorial = navigateToHistorial,
        navigateToCanje     = navigateToCanje,
        navigateToConfig    = navigateToConfig
    )
}