package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorCanjeable
import com.lepeman.pharmadatecheck.ui.theme.ColorDim

/**
 * Componente que muestra un par etiqueta-valor para datos de una política de canje.
 *
 * La etiqueta se muestra en color secundario ([ColorDim]) y el valor en color
 * de canje ([ColorCanjeable]), siguiendo la codificación cromática del sistema.
 * Usado dentro de [TarjetaPoliticaCanje] para mostrar los meses del período
 * de canje activo.
 *
 * @param etiqueta Texto descriptivo del dato (por ejemplo, "Mes 1").
 * @param valor Contenido del dato (por ejemplo, "06/2026").
 */
@Composable
fun DatoCanje(etiqueta: String, valor: String) {
    Column {
        Text(
            text       = etiqueta,
            color      = ColorDim,
            fontSize   = 11.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text       = valor,
            color      = ColorCanjeable,
            fontSize   = 13.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium
        )
    }
}