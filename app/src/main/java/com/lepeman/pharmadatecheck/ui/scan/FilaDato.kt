package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto

/**
 * Fila de dato etiqueta-valor para mostrar información de un producto clasificado.
 *
 * Presenta la [etiqueta] en color secundario ([ColorDim]) alineada a la izquierda
 * y el [valor] alineado a la derecha en [colorValor]. Usado en [TarjetaResultado]
 * para mostrar los campos del producto clasificado: nombre, laboratorio, EAN-13,
 * fecha de vencimiento, límite de canje y tiempo restante.
 *
 * @param etiqueta Texto descriptivo del dato (por ejemplo, "Laboratorio").
 * @param valor Contenido del dato a mostrar.
 * @param colorValor Color del texto del valor. Por defecto [ColorTexto]. Se puede
 * sobreescribir para resaltar datos relevantes, como la fecha límite de canje
 * en [ColorCanjeable].
 */
@Composable
fun FilaDato(
    etiqueta: String,
    valor: String,
    colorValor: Color = ColorTexto
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text       = etiqueta,
            color      = ColorDim,
            fontSize   = 13.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text       = valor,
            color      = colorValor,
            fontSize   = 13.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
fun FilaDatoPreview() {
    FilaDato(
        etiqueta = "Laboratorio",
        valor    = "RECALCINE"
    )
}