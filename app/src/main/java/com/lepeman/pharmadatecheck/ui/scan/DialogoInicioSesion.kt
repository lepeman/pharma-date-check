package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido

@Composable
fun DialogInicioSesion(
    operadorInput: String,
    onOperadorChange: (String) -> Unit,
    onConfirmar: () -> Unit,
    errorMessage: String? = null
) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.padding(24.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.inicio_sesion),
                    color = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    "Ingrese su nombre para registrar la sesión",
                    color = ColorDim,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                OutlinedTextField(
                    value = operadorInput,
                    onValueChange = onOperadorChange,
                    placeholder = { Text("Nombre del AF", color = ColorDim,
                        fontFamily = FontFamily.Monospace) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorTexto.copy(alpha = 0.4f),
                        unfocusedBorderColor = ColorBorde,
                        focusedTextColor = ColorTexto,
                        unfocusedTextColor = ColorTexto,
                        cursorColor = ColorTexto
                    )
                )

                errorMessage?.let {
                    Text(
                        text = it,
                        color = ColorVencido,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Button(
                    onClick = onConfirmar,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = operadorInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorBorde,
                        contentColor = ColorTexto
                    )
                ) {
                    Text("Iniciar", fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun DialogoInicioSesionPreview() {

    var operador by remember { mutableStateOf("") }

    DialogInicioSesion(
        operadorInput = operador,
        onOperadorChange = { operador = it },
        onConfirmar = {}
    )

}