package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.room.util.copy
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

class RutTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text
        val out = StringBuilder()

        // Lógica de formateo: 12.345.678-9
        for (i in input.indices) {
            out.append(input[i])
            val isDv = i == input.length - 2
            val reverseIndex = input.length - 1 - i

            if (input.length > 1 && reverseIndex == 1) {
                out.append("-")
            } else if (reverseIndex > 1 && (reverseIndex - 1) % 3 == 0) {
                out.append(".")
            }
        }

        val rutOffsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return offset
                val transformed = out.length - (input.length - offset)
                return transformed.coerceIn(0, out.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Relación simple para que el cursor no salte erráticamente
                var originalOffset = offset
                val textBefore = out.substring(0, offset.coerceAtMost(out.length))
                val specialChars = textBefore.count { it == '.' || it == '-' }
                return (offset - specialChars).coerceIn(0, input.length)
            }
        }

        return TransformedText(AnnotatedString(out.toString()), rutOffsetMapping)
    }
}

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
            colors = CardDefaults.cardColors(containerColor = ColorCard),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.5.dp, ColorVigente.copy(alpha = 0.5F))
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
                    text = stringResource(R.string.descripcion_inicio_sesion),
                    color = ColorDim,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                OutlinedTextField(
                    value = operadorInput,
                    onValueChange = onOperadorChange,
                    placeholder = { Text("Nombre del AF", color = ColorDim,
                        fontFamily = FontFamily.Monospace) },
                    visualTransformation = RutTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorTexto.copy(alpha = 0.4f),
                        unfocusedBorderColor = ColorVigente,
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
                        containerColor = ColorVigente,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.inicio_sesion),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DialogInicioSesionPreview() {
    PharmaDateCheckTheme {
        // En los previews de Dialogs es recomendable envolverlos en un Box o similar
        // si el renderizado del Dialog directo en el preview da problemas de visualización.
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            DialogInicioSesion(
                operadorInput = "151748309",
                onOperadorChange = {},
                onConfirmar = {}
            )
        }
    }
}