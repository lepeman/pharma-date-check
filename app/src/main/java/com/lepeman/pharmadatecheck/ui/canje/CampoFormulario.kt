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
import java.time.LocalDate

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
                Text(placeholder, color = ColorDim, fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace)
            },
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = error != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ColorTexto.copy(alpha = 0.4f),
                unfocusedBorderColor = ColorBorde,
                errorBorderColor = ColorVencido,
                focusedTextColor = ColorTexto,
                unfocusedTextColor = ColorTexto,
                cursorColor = ColorTexto
            )
        )
        error?.let {
            Text(it, color = ColorVencido, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }
    }
}