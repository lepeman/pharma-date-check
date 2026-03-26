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

@Composable
fun ChipResumen(
    etiqueta: String,
    cantidad: Int,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = cantidad.toString(),
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = etiqueta,
            color = ColorDim,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}