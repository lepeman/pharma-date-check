package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorDim

/**
 * Componente que muestra un contador de resumen con su etiqueta y color de clasificación.
 *
 * Usado en [TarjetaSesion] para mostrar los totales de productos VIGENTES,
 * CANJEABLES y VENCIDOS de una sesión de revisión. El [color] corresponde
 * a la codificación cromática del sistema: [ColorVigente], [ColorCanjeable]
 * o [ColorVencido] según la categoría representada.
 *
 * @param etiqueta Nombre de la categoría (por ejemplo, "Vigentes").
 * @param cantidad Cantidad de productos en esa categoría.
 * @param color Color asociado a la categoría de clasificación.
 */
@Composable
fun ChipResumen(
    etiqueta: String,
    cantidad: Int,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text       = cantidad.toString(),
            color      = color,
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text       = etiqueta,
            color      = ColorDim,
            fontSize   = 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}