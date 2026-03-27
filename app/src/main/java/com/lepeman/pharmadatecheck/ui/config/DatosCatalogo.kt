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

@Composable
fun DatosCatalogo(cantidad: Int, etiqueta: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = cantidad.toString(),
            color = ColorTexto,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = etiqueta,
            color = ColorDim,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun DatosCatalogoPreview() {
    PharmaDateCheckTheme {
        DatosCatalogo(20, "Productos")
    }
}