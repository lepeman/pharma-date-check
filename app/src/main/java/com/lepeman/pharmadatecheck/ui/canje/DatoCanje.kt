package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorCanjeable
import com.lepeman.pharmadatecheck.ui.theme.ColorDim

@Composable
fun DatoCanje(etiqueta: String, valor: String) {
    Column {
        Text(etiqueta, color = ColorDim, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        Text(valor, color = ColorCanjeable, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun DatoCanjePreview() {
    DatoCanje("X", "20")
}