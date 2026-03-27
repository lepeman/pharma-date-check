package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorCanjeable
import com.lepeman.pharmadatecheck.ui.theme.ColorDim

@Composable
fun DatoCanje(etiqueta: String, valor: String) {
    Column {
        Text(etiqueta, color = ColorDim, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        Text(valor, color = ColorCanjeable, fontSize = 13.sp,
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Medium)
    }
}