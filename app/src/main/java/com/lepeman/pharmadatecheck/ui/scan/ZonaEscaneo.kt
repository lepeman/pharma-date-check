package com.lepeman.pharmadatecheck.ui.scan

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto

// ── Zona de escaneo ───────────────────────────────────────────────────────────
@Composable
fun ZonaEscaneo(
    inputManual: String,
    onInputChange: (String) -> Unit,
    onConfirmarManual: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val view = LocalView.current

    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
            // Ocultar teclado virtual después de obtener el foco
            WindowCompat.getInsetsController(
                (view.context as Activity).window,
                view
            ).hide(WindowInsetsCompat.Type.ime())
        } catch (e: Exception) { }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Escanee el código de barras o ingrese el EAN-13",
                color = ColorDim,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputManual,
                    onValueChange = onInputChange,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    placeholder = {
                        Text(
                            "_ _ _ _ _ _ _ _ _ _ _ _ _",
                            color = ColorDim,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onConfirmarManual() }),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorTexto.copy(alpha = 0.4f),
                        unfocusedBorderColor = ColorBorde,
                        focusedTextColor = ColorTexto,
                        unfocusedTextColor = ColorTexto,
                        cursorColor = ColorTexto
                    )
                )
                Button(
                    onClick = onConfirmarManual,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorBorde,
                        contentColor = ColorTexto
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("OK", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview
@Composable
fun ZonaEscaneoPreview() {
    ZonaEscaneo(
        inputManual = "4525485689874",
        onInputChange = {},
        onConfirmarManual = {}
    )
}