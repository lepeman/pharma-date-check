package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

@Composable
fun BadgeContador(
    letra: String,
    cantidad: Int,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = ColorCard
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cantidad.toString(),
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp)
            )
            Text(
                text = letra,
                color = color.copy(alpha = 0.6f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun BadgeContadorPreview() {
    PharmaDateCheckTheme {
        Box(Modifier.padding(5.dp).background(ColorVigente)) {
            BadgeContador("V", 12, ColorVigente)
        }
    }
}