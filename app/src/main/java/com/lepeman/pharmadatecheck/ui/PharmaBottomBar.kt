package com.lepeman.pharmadatecheck.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente

@Composable
fun PharmaBottomAppBar(
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
        selectedTextColor = Color.White,
        selectedIconColor = ColorVigente,
        unselectedIconColor = Color.White,
        unselectedTextColor = Color.White,
        indicatorColor = ColorBorde
    )

    NavigationBar(
        containerColor = ColorVigente,
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

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PharmaBottomAppBarPreview() {
    com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme {
        PharmaBottomAppBar(
            scanSelected = true,
            historialSelected = false,
            canjeSelected = false,
            configSelected = false,
            navigateToScan = {},
            navigateToHistorial = {},
            navigateToCanje = {},
            navigateToConfig = {}
        )
    }
}