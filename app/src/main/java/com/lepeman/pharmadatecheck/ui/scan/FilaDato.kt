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

@Composable
fun FilaDato(
    etiqueta: String,
    valor: String,
    colorValor: Color = ColorTexto
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, color = ColorDim, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
        Text(valor, color = colorValor, fontSize = 13.sp,
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Medium)
    }
}

@Preview
@Composable
fun FilaDatoPreview() {
    FilaDato(
        etiqueta = "OTOC",
        valor = "$9.900"
    )
}