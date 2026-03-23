package com.lepeman.pharmadatecheck.ui.scan

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lepeman.pharmadatecheck.PharmaTopAppBar
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavigation
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.viewmodels.AppViewModelProvider
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel.ScanUiState

object ScanDestination : PharmaNavigation {
    override val route = "scan"
    override val titleRes = R.string.app_name
}

@Composable
fun ScanScreen(
    navController: NavHostController,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit,
    modifier: Modifier = Modifier,
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

    Log.d("LEPEMAN", if (sesionActivaId == null) "VERDAD" else "FALSO")

    LaunchedEffect(sesionActivaId) {
        mostrarDialogoSesion = sesionActivaId == null
    }

    var operadorInput by remember { mutableStateOf("") }

    if (mostrarDialogoSesion) {
        DialogInicioSesion(
            operadorInput = operadorInput,
            onOperadorChange = { newText ->
                if (newText.all { it.isDigit() }) {
                    operadorInput = newText
                }
            },
            onConfirmar = {
                if (operadorInput.isNotBlank()) {
                    viewModel.iniciarSesion(operadorInput.trim())
                    @Suppress("UNUSED_VALUE")
                    mostrarDialogoSesion = false
                    operadorInput = ""
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
        Text("Hola que tal", Modifier.padding(innerPadding))
    }
}


//
//
//
//    if (mostrarDialogoFecha) {
//        DialogFechaVencimiento(
//            onConfirmar = { fecha ->
//                viewModel.confirmarFechaYClasificar(ean13Pendiente, fecha)
//                mostrarDialogoFecha = false
//                ean13Pendiente = ""
//            },
//            onCancelar = {
//                mostrarDialogoFecha = false
//                ean13Pendiente = ""
//                viewModel.resetearEstado()
//            }
//        )
//    }
//
//    // Cuando se detecta un producto encontrado, mostrar diálogo de fecha
//    LaunchedEffect(scanUiState) {
//        if (scanUiState is ScanUiState.Resultado) {
//            val resultado = (scanUiState as ScanUiState.Resultado).resultado
//            if (resultado.diasRestantes == 0 && resultado.fechaLimiteCanje == null) {
//                // Es un placeholder, mostrar diálogo de fecha
//                ean13Pendiente = resultado.producto.codigoEAN13
//                mostrarDialogoFecha = true
//            }
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(ColorFondo)
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//
//        // Encabezado con contadores de sesión
//        ContadoresSesion(
//            vigentes = totalVigentes,
//            canjeables = totalCanjeables,
//            vencidos = totalVencidos,
//            onCerrarSesion = { viewModel.cerrarSesion() }
//        )
//
//        // Zona de escaneo / ingreso manual
//        ZonaEscaneo(
//            inputManual = inputManual,
//            onInputChange = { inputManual = it },
//            onConfirmarManual = {
//                viewModel.procesarEAN13Manual(inputManual.trim())
//                inputManual = ""
//            }
//        )
//
//        // Área de resultado
//        when (val state = ScanUiState) {
//            is ScanUiState.Idle -> {
//                MensajeEspera()
//            }
//            is ScanUiState.Cargando -> {
//                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                    CircularProgressIndicator(color = ColorTexto)
//                }
//            }
//            is ScanUiState.ProductoNoEncontrado -> {
//                TarjetaError("Producto no encontrado en la base de datos.\nVerifique el código EAN-13.")
//            }
//            is ScanUiState.Error -> {
//                TarjetaError(state.mensaje)
//            }
//            is ScanUiState.Resultado -> {
//                if (state.resultado.diasRestantes > 0 || state.resultado.fechaLimiteCanje != null) {
//                    TarjetaResultado(
//                        resultado = state.resultado,
//                        onNuevoEscaneo = { viewModel.resetearEstado() }
//                    )
//                }
//            }
//        }
//    }

@Composable
fun ContadoresSesion(vigentes: Int, canjeables: Int, vencidos: Int, onCerrarSesion: () -> Unit) {
    TODO("Not yet implemented")
}