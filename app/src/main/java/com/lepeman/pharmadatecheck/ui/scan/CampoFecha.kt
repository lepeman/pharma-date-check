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

@Composable
fun CampoFecha(
    placeholder: String,
    value: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= maxLength && it.all { c -> c.isDigit() }) onValueChange(it) },
        modifier = modifier,
        placeholder = { Text(placeholder, color = ColorDim, fontSize = 12.sp,
            fontFamily = FontFamily.Monospace) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ColorTexto.copy(alpha = 0.4f),
            unfocusedBorderColor = ColorBorde,
            focusedTextColor = ColorTexto,
            unfocusedTextColor = ColorTexto,
            cursorColor = ColorTexto
        )
    )
}