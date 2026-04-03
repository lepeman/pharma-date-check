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

object ScanDestination : PharmaNavigation {
    override val route = "scan"
    override val titleRes = R.string.scan_screen
}

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
    val rutaActual = navBackStackEntry?.destination
    val scanSelected = rutaActual?.hierarchy?.any { it.route == ScanDestination.route } == true

    val scanUiState by viewModel.scanUiState.collectAsState()
    val sesionUiState by viewModel.sesionUiState.collectAsState()
    val sesionActivaId by viewModel.sesionActivaId.collectAsState()
    val nombreAuxiliar = when (val state = sesionUiState) {
        is ScanViewModel.SesionUiState.Activa -> state.nombreAuxiliar
        else -> "Invitado"
    }
    val totalVigentes by viewModel.totalVigentes.collectAsState()
    val totalCanjeables by viewModel.totalCanjeables.collectAsState()
    val totalVencidos by viewModel.totalVencidos.collectAsState()

    ScanScreenContent(
        auxiliarSesion = nombreAuxiliar,
        scanSelected = scanSelected,
        scanUiState = scanUiState,
        sesionUiState = sesionUiState,
        sesionActivaId = sesionActivaId,
        totalVigentes = totalVigentes,
        totalCanjeables = totalCanjeables,
        totalVencidos = totalVencidos,
        onIniciarSesion = { viewModel.iniciarSesion(it) },
        onCerrarSesion = { viewModel.cerrarSesion() },
        onProcesarEAN13Manual = { viewModel.procesarEAN13Manual(it) },
        onResetearEstado = { viewModel.resetearEstado() },
        navigateToScan = navigateToScan,
        navigateToHistorial = navigateToHistorial,
        navigateToCanje = navigateToCanje,
        navigateToConfig = navigateToConfig
    )
}