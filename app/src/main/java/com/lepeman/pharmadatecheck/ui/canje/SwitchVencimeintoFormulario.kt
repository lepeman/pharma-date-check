package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

@Composable
fun SwitchVencimientoFormulario(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Etiquetas de texto
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Permitir canje por vencimiento",
                color = ColorTexto,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (checked) "Opción Activada" else "Opción Desactivada",
                color = if (checked) ColorBorde else ColorDim,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // El interruptor (Switch)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ColorBorde,                 // Color del círculo cuando está activo
                checkedTrackColor = ColorBorde.copy(alpha = 0.4f), // Color de la pista cuando está activo
                uncheckedThumbColor = ColorDim,               // Color del círculo cuando está inactivo
                uncheckedTrackColor = ColorCard,               // Color de la pista cuando está inactivo
                uncheckedBorderColor = ColorBorde.copy(alpha = 0.2f)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun SwitchVencimientoFormularioPreview() {
    PharmaDateCheckTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // Ejemplo de estado Activo
            SwitchVencimientoFormulario(
                checked = true,
                onCheckedChange = {}
            )
            
            // Ejemplo de estado Inactivo
            SwitchVencimientoFormulario(
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}
