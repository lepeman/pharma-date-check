package com.lepeman.pharmadatecheck.ui.canje

import android.R.attr.label
import android.R.attr.singleLine
import android.inputmethodservice.Keyboard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoFormulario(
    label: String,
    error: String? = null,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    searchQuery: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, color = ColorDim, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange(it) }
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    onQueryChange(it)
                    onExpandedChange(it.isNotEmpty())
                },
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
        }
//        OutlinedTextField(
//            value = value,
//            onValueChange = onValueChange,
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = {
//                Text(placeholder, color = ColorDim, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
//            },
//            singleLine = singleLine,
//            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
//            isError = error != null,
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = ColorTexto.copy(alpha = 0.4f),
//                unfocusedBorderColor = ColorBorde,
//                errorBorderColor = ColorVencido,
//                focusedTextColor = ColorTexto,
//                unfocusedTextColor = ColorTexto,
//                cursorColor = ColorTexto
//            )
//        )
        error?.let {
            Text(it, color = ColorVencido, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun CampoFormularioPreview() {
    PharmaDateCheckTheme {
        CampoFormulario(
            label = "LABORATORIO",
            placeholder = "Escribe el nombre...",
            error = "Este campo es obligatorio",
            searchQuery = "",
            expanded = false,
            onExpandedChange = {},
            onQueryChange = {}
        )
    }
}