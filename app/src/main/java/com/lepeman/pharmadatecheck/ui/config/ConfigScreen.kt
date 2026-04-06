package com.lepeman.pharmadatecheck.ui.config

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavigation
import com.lepeman.pharmadatecheck.ui.viewmodels.AppViewModelProvider
import com.lepeman.pharmadatecheck.ui.viewmodels.ConfigViewModel

/** Destino de navegación para la pantalla de configuración. */
object ConfigDestination : PharmaNavigation {
    override val route = "config"
    override val titleRes = R.string.config
}

/**
 * Pantalla de configuración de la aplicación.
 *
 * Actúa como contenedor de estado (stateful), recopilando los valores
 * expuestos por [ConfigViewModel] y delegando el renderizado a
 * [ConfigScreenContent], que es un composable sin estado (stateless).
 *
 * Gestiona dos launchers de selección de archivos mediante
 * [ActivityResultContracts.GetContent]: uno para importar el catálogo
 * de productos desde CSV y otro para importar el catálogo de laboratorios.
 * Cuando el usuario selecciona un archivo, invoca el método correspondiente
 * del ViewModel con el URI obtenido.
 *
 * @param navController Controlador de navegación para determinar la ruta
 * activa en la barra de navegación inferior.
 * @param navigateToScan Acción para navegar a la pantalla de escaneo.
 * @param navigateToHistorial Acción para navegar a la pantalla de historial.
 * @param navigateToCanje Acción para navegar a la pantalla de canjes.
 * @param navigateToConfig Acción para navegar a esta misma pantalla.
 * @param viewModel ViewModel que gestiona el estado de la pantalla.
 */
@Composable
fun ConfigScreen(
    navController: NavHostController,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit,
    viewModel: ConfigViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual    = navBackStackEntry?.destination
    val configSelected = rutaActual?.hierarchy?.any { it.route == ConfigDestination.route } == true

    val importStateProductos    by viewModel.importStateProductos.collectAsState()
    val importStateLaboratorios by viewModel.importStateLaboratorios.collectAsState()
    val totalProductos          by viewModel.totalProductos.collectAsState()
    val totalLaboratorios       by viewModel.totalLaboratorios.collectAsState()
    val context = LocalContext.current

    // Launcher para seleccionar el CSV de productos
    val launcherProductos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.importarProductosCSV(context, it) }
    }

    // Launcher para seleccionar el CSV de laboratorios
    val launcherLaboratorios = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.importarLaboratoriosCSV(context, it) }
    }

    ConfigScreenContent(
        configSelected             = configSelected,
        totalProductos             = totalProductos,
        totalLaboratorios          = totalLaboratorios,
        launcherStateProductos     = launcherProductos,
        launcherStateLaboratorios  = launcherLaboratorios,
        importStateProductos       = importStateProductos,
        importStateLaboratorios    = importStateLaboratorios,
        navigateToScan             = navigateToScan,
        navigateToHistorial        = navigateToHistorial,
        navigateToCanje            = navigateToCanje,
        navigateToConfig           = navigateToConfig,
        onResetearEstadoProductos  = { viewModel.resetearEstadoProductos() },
        onResetearEstadoLaboratorios = { viewModel.resetearEstadoLaboratorios() }
    )
}