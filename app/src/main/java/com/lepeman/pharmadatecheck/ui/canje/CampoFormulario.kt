package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente

/**
 * Campo de texto genérico para formularios con soporte de etiqueta,
 * placeholder, validación de error y estado deshabilitado.
 *
 * Usado en el formulario de políticas de canje para los campos de mes
 * de período de canje (mesUno, mesDos, mesTres).
 *
 * @param label Etiqueta descriptiva mostrada sobre el campo.
 * @param value Texto actualmente ingresado.
 * @param onValueChange Callback invocado al cambiar el texto del campo.
 * @param error Mensaje de error a mostrar bajo el campo. Null si no hay error.
 * @param placeholder Texto de ayuda mostrado cuando el campo está vacío.
 * @param keyboardType Tipo de teclado a mostrar. Por defecto [KeyboardType.Text].
 * @param singleLine Si es true, el campo ocupa una sola línea.
 * @param enabled Si es false, el campo se muestra deshabilitado visualmente
 * y no acepta entrada del usuario.
 */
@Composable
fun CampoFormulario(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            color = if (enabled) ColorDim else ColorDim.copy(alpha = 0.4f),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            placeholder = {
                Text(
                    text = placeholder,
                    color = ColorDim,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            },
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = error != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ColorVigente,
                unfocusedBorderColor = ColorBorde,
                errorBorderColor = ColorVencido,
                focusedTextColor = ColorTexto,
                unfocusedTextColor = ColorTexto,
                cursorColor = ColorVigente,
                focusedLabelColor = ColorVigente,
                disabledBorderColor = ColorBorde.copy(alpha = 0.4f),
                disabledTextColor = ColorTexto.copy(alpha = 0.4f)
            )
        )
        error?.let {
            Text(
                text = it,
                color = ColorVencido,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}