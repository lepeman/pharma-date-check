package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lepeman.pharmadatecheck.PharmaTopAppBar
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.config.ConfigDestination
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavigation
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme
import com.lepeman.pharmadatecheck.ui.viewmodels.AppViewModelProvider
import com.lepeman.pharmadatecheck.ui.viewmodels.CanjeViewModel
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel
import java.nio.file.WatchEvent

object CanjeDestination : PharmaNavigation {
    override val route = "canje"
    override val titleRes = R.string.canje
}

@Composable
fun CanjeScreen(
    navController: NavHostController,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit,
    viewModel: CanjeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination

    val uiState by viewModel.uiState.collectAsState()
    val formulario by viewModel.formulario.collectAsState()
    val politicaAEliminar by viewModel.politicaAEliminar.collectAsState()

    if (formulario is CanjeViewModel.FormularioState.Visible) {
        val form = formulario as CanjeViewModel.FormularioState.Visible
        DialogFormularioCanje(
            form = form,
            onLaboratorioChange = viewModel::onLaboratorioChange,
            onDiasChange = {},
            onVencimientoChange = {},
            onCondicionesChange = {},
            onGuardar = {},
            onCancelar = {}
        )
    }

    politicaAEliminar?.let { politica ->
        DialogConfirmarEliminar(
            laboratorio = politica.laboratorioId.toString(),
            onConfirmar = viewModel::confirmarEliminar,
            onCancelar = viewModel::cancelarEliminar
        )
    }

    Scaffold(
        bottomBar = {
            PharmaTopAppBar(
                canjeSelected = rutaActual?.hierarchy?.any { it.route == CanjeDestination.route } == true,
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_politicas),
                    color = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = stringResource(R.string.descripcion_canje),
                    color = ColorDim,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace
                )

                HorizontalDivider(color = ColorBorde)

                when (val state = uiState) {
                    is CanjeViewModel.UiState.Cargando -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = ColorTexto)
                        }
                    }
                    is CanjeViewModel.UiState.Vacio -> {
                        EstadoVacioCanje(
                            onPrepoblar = viewModel::prepoblarSiVacio
                        )
                    }
                    is CanjeViewModel.UiState.ConDatos -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(state.politicas, key = { it.id }) { politica ->
                                TarjetaPoliticaCanje(
                                    politica = politica,
                                    onEditar = { viewModel.abrirFormularioEdicion(politica) },
                                    onEliminar = { viewModel.solicitarEliminar(politica) }
                                )
                            }
                        }
                    }
                    is CanjeViewModel.UiState.Error -> {
                        TarjetaErrorCanje(state.mensaje)
                    }
                }
            }

            FloatingActionButton(
                onClick = viewModel::abrirFormularioNuevo,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                containerColor = ColorBorde,
                contentColor = ColorTexto
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.nueva_politica))
            }
        }
    }
}
