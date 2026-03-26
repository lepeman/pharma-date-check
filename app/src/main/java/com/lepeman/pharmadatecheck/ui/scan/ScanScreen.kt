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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lepeman.pharmadatecheck.PharmaTopAppBar
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavigation
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel.ScanUiState
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

    val scanUiState by viewModel.scanUiState.collectAsState()
    val sesionUiState by viewModel.sesionUiState.collectAsState()
    val sesionActivaId by viewModel.sesionActivaId.collectAsState()
    val totalVigentes by viewModel.totalVigentes.collectAsState()
    val totalCanjeables by viewModel.totalCanjeables.collectAsState()
    val totalVencidos by viewModel.totalVencidos.collectAsState()

    val errorSesion = (sesionUiState as? ScanViewModel.SesionUiState.Error)?.mensaje

    var inputManual by remember { mutableStateOf("") }
    var mostrarDialogoFecha by remember { mutableStateOf(false) }
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
                    viewModel.iniciarSesion(rutAuxiliarInput.trim())
                    @Suppress("UNUSED_VALUE")
                    mostrarDialogoSesion = false
                    rutAuxiliarInput = ""
                }
            },
            errorMessage = errorSesion
        )
    }


    Scaffold(
        bottomBar = {
            PharmaTopAppBar(
                scanSelected = rutaActual?.hierarchy?.any { it.route == ScanDestination.route } == true,
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
                vigentes = totalVigentes,
                canjeables = totalCanjeables,
                vencidos = totalVencidos,
                onCerrarSesion = { viewModel.cerrarSesion() }
            )

            ZonaEscaneo(
                inputManual = inputManual,
                onInputChange = { inputManual = it },
                onConfirmarManual = {
                    viewModel.procesarEAN13Manual(inputManual.trim())
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
                is ScanUiState.Error -> { state.mensaje }
                is ScanUiState.Resultado -> {
                    TarjetaResultado(
                        resultado = state.resultado,
                        onNuevoEscaneo = { viewModel.resetearEstado() }
                    )
                }
            }
        }
    }
}