package com.lepeman.pharmadatecheck

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavHost
import com.lepeman.pharmadatecheck.ui.scan.ScanDestination
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import kotlinx.coroutines.selects.select

@Composable
fun MainApp(navController: NavHostController = rememberNavController()) {
    PharmaNavHost(navController = navController)
}

@Composable
fun PharmaTopAppBar(
    scanSelected: Boolean = false,
    historialSelected: Boolean = false,
    canjeSelected: Boolean = false,
    configSelected: Boolean = false,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit
) {

    val itemColors = NavigationBarItemDefaults.colors(
        selectedTextColor = ColorTexto,
        unselectedIconColor = ColorDim,
        unselectedTextColor = ColorDim,
        indicatorColor = ColorBorde
    )

    NavigationBar(
        containerColor = ColorCard,
        tonalElevation = androidx.compose.ui.unit.Dp.Unspecified
    ) {
        NavigationBarItem(
            selected = scanSelected,
            onClick = navigateToScan,
            icon = {
                Icon(
                    painterResource(R.drawable.ic_scan),
                    contentDescription = stringResource(R.string.scan_screen)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.scan_screen),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
            },
            colors = itemColors
        )
        NavigationBarItem(
            selected = historialSelected,
            onClick = navigateToHistorial,
            icon = {
                Icon(
                    painterResource(R.drawable.ic_historial),
                    contentDescription = stringResource(R.string.historial_screen)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.historial_screen),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
            },
            colors = itemColors
        )
        NavigationBarItem(
            selected = canjeSelected,
            onClick = navigateToCanje,
            icon = {
                Icon(
                    painterResource(R.drawable.ic_canje),
                    contentDescription = stringResource(R.string.canje_screen)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.canje_screen),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
            },
            colors = itemColors
        )
        NavigationBarItem(
            selected = configSelected,
            onClick = navigateToConfig,
            icon = {
                Icon(
                    painterResource(R.drawable.ic_config),
                    contentDescription = stringResource(R.string.config_screen)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.config_screen),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
            },
            colors = itemColors
        )
    }
}