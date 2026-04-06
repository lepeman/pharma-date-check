package com.lepeman.pharmadatecheck.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Esquema de colores claro de la aplicación, alineado con la identidad
 * corporativa de Cruz Verde y el fondo blanco adoptado en la UI.
 *
 * Se usa [lightColorScheme] para que Material3 derive correctamente los
 * colores de estado, elevación y contenido sobre superficies claras.
 */
private val LightColorScheme = lightColorScheme(
    primary      = ColorVigente,
    background   = ColorFondo,
    surface      = ColorCard,
    onPrimary    = ColorFondo,
    onBackground = ColorTexto,
    onSurface    = ColorTexto
)

/**
 * Tema principal de PharmaDateCheck.
 *
 * Aplica el esquema de colores corporativo de Cruz Verde sobre Material3,
 * configura el color de la barra de estado en blanco ([ColorFondo]) y
 * establece los iconos de la barra de estado en modo oscuro
 * ([isAppearanceLightStatusBars] = true) para garantizar su visibilidad
 * sobre fondo blanco.
 *
 * @param content Contenido composable al que se aplica el tema.
 */
@Composable
fun PharmaDateCheckTheme(content: @Composable () -> Unit) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ColorFondo.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = Typography,
        content     = content
    )
}