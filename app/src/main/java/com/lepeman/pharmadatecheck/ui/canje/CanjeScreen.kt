package com.lepeman.pharmadatecheck.ui.canje

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
import com.lepeman.pharmadatecheck.ui.viewmodels.CanjeViewModel

/** Destino de navegación para la pantalla de gestión de políticas de canje. */
object CanjeDestination : PharmaNavigation {
    override val route = "canje"
    override val titleRes = R.string.canje
}

/**
 * Pantalla de gestión de políticas de canje.
 *
 * Actúa como contenedor de estado (stateful), recopilando los valores
 * expuestos por [CanjeViewModel] y delegando el renderizado a
 * [CanjeScreenContent], que es un composable sin estado (stateless).
 *
 * @param navController Controlador de navegación para determinar la ruta activa
 * en la barra de navegación inferior.
 * @param navigateToScan Acción para navegar a la pantalla de escaneo.
 * @param navigateToHistorial Acción para navegar a la pantalla de historial.
 * @param navigateToCanje Acción para navegar a esta misma pantalla.
 * @param navigateToConfig Acción para navegar a la pantalla de configuración.
 * @param viewModel ViewModel que gestiona el estado de la pantalla.
 */
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
    val rutaActual        = navBackStackEntry?.destination
    val canjeSelected     = rutaActual?.hierarchy?.any { it.route == CanjeDestination.route } == true

    val uiState           by viewModel.uiState.collectAsState()
    val formulario        by viewModel.formulario.collectAsState()
    val politicaAEliminar by viewModel.politicaAEliminar.collectAsState()

    val sugerenciasEmpresas     by viewModel.sugerenciasEmpresas.collectAsState()
    val expandedEmpresa         by viewModel.expandedEmpresa.collectAsState()
    val sugerenciasLaboratorios by viewModel.sugerenciasLaboratorios.collectAsState()
    val expandedLaboratorio     by viewModel.expandedLaboratorio.collectAsState()

    val textoMesUno  by viewModel.textoMesUno.collectAsState()
    val textoMesDos  by viewModel.textoMesDos.collectAsState()
    val textoMesTres by viewModel.textoMesTres.collectAsState()

    CanjeScreenContent(
        canjeSelected               = canjeSelected,
        uiState                     = uiState,
        formulario                  = formulario,
        politicaAEliminar           = politicaAEliminar,
        sugerenciasEmpresas         = sugerenciasEmpresas,
        expandedEmpresa             = expandedEmpresa,
        sugerenciasLaboratorios     = sugerenciasLaboratorios,
        expandedLaboratorio         = expandedLaboratorio,
        textoMesUno                 = textoMesUno,
        textoMesDos                 = textoMesDos,
        textoMesTres                = textoMesTres,
        onNuevaPolitica             = viewModel::abrirFormularioNuevo,
        onEditarPolitica            = viewModel::abrirFormularioEdicion,
        onEliminarPolitica          = viewModel::solicitarEliminar,
        onConfirmarEliminar         = viewModel::confirmarEliminar,
        onCancelarEliminar          = viewModel::cancelarEliminar,
        onPrepoblar                 = viewModel::prepoblarSiVacio,
        onEmpresaChange             = viewModel::onEmpresaChange,
        onEmpresaSeleccionada       = viewModel::onEmpresaSeleccionada,
        onExpandedEmpresaChange     = viewModel::onExpandedEmpresaChange,
        onLaboratorioChange         = viewModel::onLaboratorioChange,
        onLaboratorioSeleccionado   = viewModel::onLaboratorioSeleccionado,
        onExpandedLaboratorioChange = viewModel::onExpandedLaboratorioChange,
        onVencimientoChange         = viewModel::onVencimientoChange,
        onMesUnoChange              = viewModel::onMesUnoChange,
        onMesDosChange              = viewModel::onMesDosChange,
        onMesTresChange             = viewModel::onMesTresChange,
        onGuardar                   = viewModel::guardarPolitica,
        onCerrarFormulario          = viewModel::cerrarFormulario,
        navigateToScan              = navigateToScan,
        navigateToHistorial         = navigateToHistorial,
        navigateToCanje             = navigateToCanje,
        navigateToConfig            = navigateToConfig
    )
}