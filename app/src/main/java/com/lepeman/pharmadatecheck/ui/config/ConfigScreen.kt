package com.lepeman.pharmadatecheck.ui.config

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lepeman.pharmadatecheck.PharmaTopAppBar
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.historial.HistorialDestination
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavigation
import com.lepeman.pharmadatecheck.ui.viewmodels.AppViewModelProvider
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel

object ConfigDestination : PharmaNavigation {
    override val route = "config"
    override val titleRes = R.string.config
}


@Composable
fun ConfigScreen(
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

    Scaffold(
        bottomBar = {
            PharmaTopAppBar(
                configSelected = rutaActual?.hierarchy?.any { it.route == ConfigDestination.route } == true,
                navigateToScan = navigateToScan,
                navigateToHistorial = navigateToHistorial,
                navigateToCanje = navigateToCanje,
                navigateToConfig = navigateToConfig
            )
        }
    ) { innerPadding ->
        Text("Pantalla Configuración", Modifier.padding(innerPadding))
    }
}