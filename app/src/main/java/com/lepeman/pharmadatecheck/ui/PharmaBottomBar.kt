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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

/**
 * Barra de navegación inferior de la aplicación.
 *
 * Presenta cuatro ítems de navegación — Escaneo, Historial, Canje y
 * Configuración — con fondo verde corporativo ([ColorVigente]) e iconos
 * y texto en blanco. El ítem activo se resalta con un indicador blanco
 * translúcido y el ícono en color diferenciado.
 *
 * Cada parámetro `xSelected` indica si ese ítem es la ruta activa,
 * determinando el estado visual del [NavigationBarItem] correspondiente.
 *
 * @param scanSelected Indica si la pantalla de escaneo es la ruta activa.
 * @param historialSelected Indica si la pantalla de historial es la ruta activa.
 * @param canjeSelected Indica si la pantalla de canjes es la ruta activa.
 * @param configSelected Indica si la pantalla de configuración es la ruta activa.
 * @param navigateToScan Acción para navegar a la pantalla de escaneo.
 * @param navigateToHistorial Acción para navegar a la pantalla de historial.
 * @param navigateToCanje Acción para navegar a la pantalla de canjes.
 * @param navigateToConfig Acción para navegar a la pantalla de configuración.
 */
@Composable
fun PharmaBottomAppBar(
    scanSelected: Boolean      = false,
    historialSelected: Boolean = false,
    canjeSelected: Boolean     = false,
    configSelected: Boolean    = false,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit
) {
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor   = Color.White,
        selectedTextColor   = Color.White,
        unselectedIconColor = Color.White.copy(alpha = 0.6f),
        unselectedTextColor = Color.White.copy(alpha = 0.6f),
        indicatorColor      = Color.White.copy(alpha = 0.2f)
    )

    NavigationBar(
        containerColor = ColorVigente,
        tonalElevation = androidx.compose.ui.unit.Dp.Unspecified
    ) {
        NavigationBarItem(
            selected = scanSelected,
            onClick  = navigateToScan,
            icon     = {
                Icon(
                    painter            = painterResource(R.drawable.ic_scan),
                    contentDescription = stringResource(R.string.scan_screen)
                )
            },
            label  = {
                Text(
                    text       = stringResource(R.string.scan_screen),
                    fontFamily = FontFamily.Monospace,
                    fontSize   = 10.sp
                )
            },
            colors = itemColors
        )
        NavigationBarItem(
            selected = historialSelected,
            onClick  = navigateToHistorial,
            icon     = {
                Icon(
                    painter            = painterResource(R.drawable.ic_historial),
                    contentDescription = stringResource(R.string.historial_screen)
                )
            },
            label  = {
                Text(
                    text       = stringResource(R.string.historial_screen),
                    fontFamily = FontFamily.Monospace,
                    fontSize   = 10.sp
                )
            },
            colors = itemColors
        )
        NavigationBarItem(
            selected = canjeSelected,
            onClick  = navigateToCanje,
            icon     = {
                Icon(
                    painter            = painterResource(R.drawable.ic_canje),
                    contentDescription = stringResource(R.string.canje_screen)
                )
            },
            label  = {
                Text(
                    text       = stringResource(R.string.canje_screen),
                    fontFamily = FontFamily.Monospace,
                    fontSize   = 10.sp
                )
            },
            colors = itemColors
        )
        NavigationBarItem(
            selected = configSelected,
            onClick  = navigateToConfig,
            icon     = {
                Icon(
                    painter            = painterResource(R.drawable.ic_config),
                    contentDescription = stringResource(R.string.config_screen)
                )
            },
            label  = {
                Text(
                    text       = stringResource(R.string.config_screen),
                    fontFamily = FontFamily.Monospace,
                    fontSize   = 10.sp
                )
            },
            colors = itemColors
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PharmaBottomAppBarPreview() {
    PharmaDateCheckTheme {
        PharmaBottomAppBar(
            scanSelected      = true,
            navigateToScan    = {},
            navigateToHistorial = {},
            navigateToCanje   = {},
            navigateToConfig  = {}
        )
    }
}