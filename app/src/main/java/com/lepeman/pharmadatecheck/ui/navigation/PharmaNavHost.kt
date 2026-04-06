package com.lepeman.pharmadatecheck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lepeman.pharmadatecheck.ui.canje.CanjeDestination
import com.lepeman.pharmadatecheck.ui.canje.CanjeScreen
import com.lepeman.pharmadatecheck.ui.config.ConfigDestination
import com.lepeman.pharmadatecheck.ui.config.ConfigScreen
import com.lepeman.pharmadatecheck.ui.historial.HistorialDestination
import com.lepeman.pharmadatecheck.ui.historial.HistorialScreen
import com.lepeman.pharmadatecheck.ui.scan.ScanDestination
import com.lepeman.pharmadatecheck.ui.scan.ScanScreen

/**
 * Grafo de navegación principal de la aplicación.
 *
 * Define las cuatro rutas navegables de la aplicación — Escaneo, Historial,
 * Canje y Configuración — y conecta cada pantalla con las acciones de
 * navegación de la barra inferior. Cada acción de navegación usa
 * [popUpTo] con [saveState] y [restoreState] para mantener el estado de
 * cada pantalla al cambiar de pestaña, evitando recomposiciones innecesarias
 * y preservando el scroll y los datos cargados.
 *
 * La ruta inicial es [ScanDestination], que corresponde a la pantalla de
 * escaneo de productos.
 *
 * @param navController Controlador de navegación compartido con todas las pantallas.
 * @param modifier Modificador opcional para personalizar el layout del host.
 */
@Composable
fun PharmaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController    = navController,
        startDestination = ScanDestination.route,
        modifier         = modifier
    ) {
        composable(route = ScanDestination.route) {
            ScanScreen(
                navController       = navController,
                navigateToScan      = { navController.navigateSingleTop(ScanDestination.route) },
                navigateToHistorial = { navController.navigateSingleTop(HistorialDestination.route) },
                navigateToCanje     = { navController.navigateSingleTop(CanjeDestination.route) },
                navigateToConfig    = { navController.navigateSingleTop(ConfigDestination.route) }
            )
        }
        composable(route = HistorialDestination.route) {
            HistorialScreen(
                navController       = navController,
                navigateToScan      = { navController.navigateSingleTop(ScanDestination.route) },
                navigateToHistorial = { navController.navigateSingleTop(HistorialDestination.route) },
                navigateToCanje     = { navController.navigateSingleTop(CanjeDestination.route) },
                navigateToConfig    = { navController.navigateSingleTop(ConfigDestination.route) }
            )
        }
        composable(route = CanjeDestination.route) {
            CanjeScreen(
                navController       = navController,
                navigateToScan      = { navController.navigateSingleTop(ScanDestination.route) },
                navigateToHistorial = { navController.navigateSingleTop(HistorialDestination.route) },
                navigateToCanje     = { navController.navigateSingleTop(CanjeDestination.route) },
                navigateToConfig    = { navController.navigateSingleTop(ConfigDestination.route) }
            )
        }
        composable(route = ConfigDestination.route) {
            ConfigScreen(
                navController       = navController,
                navigateToScan      = { navController.navigateSingleTop(ScanDestination.route) },
                navigateToHistorial = { navController.navigateSingleTop(HistorialDestination.route) },
                navigateToCanje     = { navController.navigateSingleTop(CanjeDestination.route) },
                navigateToConfig    = { navController.navigateSingleTop(ConfigDestination.route) }
            )
        }
    }
}

/**
 * Extensión que centraliza la lógica de navegación entre pestañas.
 *
 * Navega a [route] limpiando el back stack hasta el destino inicial del grafo,
 * con [saveState] y [restoreState] activos para preservar el estado de cada
 * pantalla al cambiar de pestaña. [launchSingleTop] evita crear múltiples
 * instancias del mismo destino si el usuario toca la pestaña activa.
 */
private fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState    = true
    }
}