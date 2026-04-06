package com.lepeman.pharmadatecheck.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Tipografía de la aplicación basada en Material3.
 *
 * Define únicamente [Typography.bodyLarge] con los valores por defecto
 * de Material3. El resto de estilos tipográficos de la aplicación se
 * especifican directamente en los composables mediante [FontFamily.Monospace],
 * que es la tipografía adoptada en toda la interfaz para mantener consistencia
 * visual con la naturaleza técnica y operacional de la aplicación.
 */
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Normal,
        fontSize      = 16.sp,
        lineHeight    = 24.sp,
        letterSpacing = 0.5.sp
    )
)