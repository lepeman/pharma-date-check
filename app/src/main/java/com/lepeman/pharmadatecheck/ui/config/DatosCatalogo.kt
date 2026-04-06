package com.lepeman.pharmadatecheck.ui.config

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

/**
 * Componente que muestra una métrica de catálogo con su cantidad y etiqueta.
 *
 * Presenta la [cantidad] en tipografía grande y prominente y la [etiqueta]
 * descriptiva debajo en tamaño reducido. Usado en [ConfigScreenContent] para
 * mostrar el total de productos y laboratorios registrados en la base de datos.
 *
 * @param cantidad Valor numérico a mostrar.
 * @param etiqueta Descripción del dato mostrado (por ejemplo, "productos").
 */
@Composable
fun DatosCatalogo(cantidad: Int, etiqueta: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text       = cantidad.toString(),
            color      = ColorTexto,
            fontWeight = FontWeight.Bold,
            fontSize   = 32.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text       = etiqueta,
            color      = ColorDim,
            fontSize   = 12.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DatosCatalogoPreview() {
    PharmaDateCheckTheme {
        DatosCatalogo(cantidad = 20, etiqueta = "Productos")
    }
}