package com.lepeman.pharmadatecheck.ui.scan

import android.app.Activity
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
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
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente

/**
 * Zona de captura de código EAN-13 en la pantalla de escaneo.
 *
 * Combina un [OutlinedTextField] con un botón OK para soportar tanto la
 * captura mediante lector HID como el ingreso manual del código. Al
 * inicializarse solicita el foco automáticamente y oculta el teclado
 * virtual, de modo que el lector HID pueda enviar caracteres directamente
 * sin que el teclado de pantalla interfiera con la interfaz.
 *
 * El campo acepta únicamente entrada numérica ([KeyboardType.NumberPassword])
 * y confirma la entrada al pulsar la tecla Done del teclado o el botón OK.
 *
 * @param inputManual Texto actualmente ingresado en el campo.
 * @param onInputChange Callback invocado al cambiar el texto del campo.
 * @param onConfirmarManual Callback invocado al confirmar el código ingresado,
 * ya sea mediante el botón OK o la tecla Done del teclado.
 */
@Composable
fun ZonaEscaneo(
    inputManual: String,
    onInputChange: (String) -> Unit,
    onConfirmarManual: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val view           = LocalView.current

    // Solicita el foco y oculta el teclado virtual al inicializarse
    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
            WindowCompat.getInsetsController(
                (view.context as Activity).window,
                view
            ).hide(WindowInsetsCompat.Type.ime())
        } catch (e: Exception) { }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape  = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, ColorVigente.copy(alpha = 0.5f))
    ) {
        Column(
            modifier            = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text       = "Escanee el código de barras o ingrese el EAN-13",
                color      = ColorDim,
                fontSize   = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value         = inputManual,
                    onValueChange = onInputChange,
                    modifier      = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    placeholder = {
                        Text(
                            text       = "_ _ _ _ _ _ _ _ _ _ _ _ _",
                            color      = ColorDim,
                            fontFamily = FontFamily.Monospace,
                            fontSize   = 13.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction    = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onConfirmarManual() }),
                    singleLine      = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = ColorVigente,
                        unfocusedBorderColor = ColorBorde,
                        focusedTextColor     = ColorTexto,
                        unfocusedTextColor   = ColorTexto,
                        cursorColor          = ColorVigente
                    )
                )
                Button(
                    onClick = onConfirmarManual,
                    colors  = ButtonDefaults.buttonColors(
                        containerColor = ColorVigente,
                        contentColor   = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text       = "OK",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ZonaEscaneoPreview() {
    ZonaEscaneo(
        inputManual       = "4525485689874",
        onInputChange     = {},
        onConfirmarManual = {}
    )
}