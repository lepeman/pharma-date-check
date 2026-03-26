package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.runtime.Composable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto

@Composable
fun DialogConfirmarEliminarSesion(
    operador: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        containerColor = ColorCard,
        title = {
            Text(
                text = stringResource(R.string.eliminar_sesion),
                color = ColorTexto,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(R.string.confirmar_aliminar_sesion),
                color = ColorDim,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text(
                    text = stringResource(R.string.texto_boton_eliminar),
                    color = ColorDim,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onConfirmar) {
                TextButton(onClick = onCancelar) {
                    Text(
                        text = stringResource(R.string.texto_cancelar),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    )
}