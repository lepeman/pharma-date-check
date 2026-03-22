package com.lepeman.pharmadatecheck.ui.canje

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.CanvasHolder
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

@Composable
fun CampoFormulario(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, color = ColorDim, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(placeholder, color = ColorDim, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
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

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun CampoFormularioPreview() {
    PharmaDateCheckTheme {

        var textoSimulado by remember {
            mutableStateOf("Resultado")
        }

        CampoFormulario(
            label = "Tarjeta Error",
            value = textoSimulado,
            onValueChange = { nuevoValor ->
                textoSimulado = nuevoValor
            },
            singleLine = false
        )
    }
}