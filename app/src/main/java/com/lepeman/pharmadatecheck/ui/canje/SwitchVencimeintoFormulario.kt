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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

/**
 * Interruptor para habilitar o deshabilitar la política de canje por vencimiento.
 *
 * Muestra una etiqueta principal y un texto de estado secundario que cambia
 * según el valor de [checked]. Cuando está activo, los campos de mes del
 * período de canje se habilitan en [DialogFormularioCanje]; cuando está
 * inactivo, dichos campos se deshabilitan y sus valores son ignorados
 * tanto en la persistencia como en la clasificación.
 *
 * @param checked Estado actual del interruptor.
 * @param onCheckedChange Callback invocado al cambiar el estado del interruptor.
 * @param modifier Modificador opcional para personalizar el layout del componente.
 */
@Composable
fun SwitchVencimientoFormulario(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = "Permitir canje por vencimiento",
                color      = ColorTexto,
                fontSize   = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(
                text       = if (checked) "Opción Activada" else "Opción Desactivada",
                color      = if (checked) ColorVigente else ColorDim,
                fontSize   = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        Switch(
            checked        = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor    = Color.White,
                checkedTrackColor    = ColorVigente,
                uncheckedThumbColor  = ColorDim,
                uncheckedTrackColor  = ColorCard,
                uncheckedBorderColor = ColorBorde
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SwitchVencimientoFormularioPreview() {
    PharmaDateCheckTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            SwitchVencimientoFormulario(checked = true,  onCheckedChange = {})
            SwitchVencimientoFormulario(checked = false, onCheckedChange = {})
        }
    }
}