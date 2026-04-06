package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente

/**
 * Campo de texto numérico con longitud máxima restringida.
 *
 * Acepta únicamente dígitos y limita la entrada a [maxLength] caracteres,
 * rechazando cualquier carácter no numérico o que exceda el límite en el
 * callback [onValueChange]. Usado en [DialogoFechaVencimiento] para los
 * campos de mes y año del diálogo de confirmación de fecha de vencimiento.
 *
 * @param placeholder Texto de ayuda mostrado cuando el campo está vacío.
 * @param value Texto actualmente ingresado.
 * @param maxLength Número máximo de caracteres permitidos.
 * @param modifier Modificador opcional para personalizar el layout del campo.
 * @param onValueChange Callback invocado cuando el texto cambia y cumple las
 * restricciones de longitud y formato numérico.
 */
@Composable
fun CampoFecha(
    placeholder: String,
    value: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value         = value,
        onValueChange = { if (it.length <= maxLength && it.all { c -> c.isDigit() }) onValueChange(it) },
        modifier      = modifier,
        placeholder   = {
            Text(
                text       = placeholder,
                color      = ColorDim,
                fontSize   = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine      = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = ColorVigente,
            unfocusedBorderColor = ColorBorde,
            focusedTextColor     = ColorTexto,
            unfocusedTextColor   = ColorTexto,
            cursorColor          = ColorVigente
        )
    )
}