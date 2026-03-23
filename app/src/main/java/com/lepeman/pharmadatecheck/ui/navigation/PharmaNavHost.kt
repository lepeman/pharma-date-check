package com.lepeman.pharmadatecheck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.lepeman.pharmadatecheck.ui.canje.CanjeDestination
import com.lepeman.pharmadatecheck.ui.canje.CanjeScreen
import com.lepeman.pharmadatecheck.ui.config.ConfigDestination
import com.lepeman.pharmadatecheck.ui.config.ConfigScreen
import com.lepeman.pharmadatecheck.ui.historial.HistorialDestination
import com.lepeman.pharmadatecheck.ui.historial.HistorialScreen
import com.lepeman.pharmadatecheck.ui.scan.ScanDestination
import com.lepeman.pharmadatecheck.ui.scan.ScanScreen

@Composable
fun PharmaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ScanDestination.route,
        modifier = modifier
    ) {
        composable(route = ScanDestination.route) {
            ScanScreen(
                navController = navController,
                navigateToScan = {
                    navController.navigate(ScanDestination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToHistorial = {
                    navController.navigate(HistorialDestination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToCanje = {
                    navController.navigate(CanjeDestination.route)  {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToConfig = {
                    navController.navigate(ConfigDestination.route)  {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                }
            )
        }
        composable(route = HistorialDestination.route) {
            HistorialScreen(
                navController = navController,
                navigateToScan = {
                    navController.navigate(ScanDestination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToHistorial = {
                    navController.navigate(HistorialDestination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToCanje = {
                    navController.navigate(CanjeDestination.route)  {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToConfig = {
                    navController.navigate(ConfigDestination.route)  {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                }
            )
        }
        composable(route = CanjeDestination.route) {
            CanjeScreen(
                navController = navController,
                navigateToScan = {
                    navController.navigate(ScanDestination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToHistorial = {
                    navController.navigate(HistorialDestination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToCanje = {
                    navController.navigate(CanjeDestination.route)  {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToConfig = {
                    navController.navigate(ConfigDestination.route)  {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                }
            )
        }

        composable(route = ConfigDestination.route) {
            ConfigScreen(
                navController = navController,
                navigateToScan = {
                    navController.navigate(ScanDestination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToHistorial = {
                    navController.navigate(HistorialDestination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToCanje = {
                    navController.navigate(CanjeDestination.route)  {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                navigateToConfig = {
                    navController.navigate(ConfigDestination.route)  {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                }
            )
        }
    }
}