package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido

@Composable
fun TarjetaErrorHistorial(mensaje: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorVencido.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = mensaje,
            color = ColorVencido,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(16.dp)
        )
    }
}