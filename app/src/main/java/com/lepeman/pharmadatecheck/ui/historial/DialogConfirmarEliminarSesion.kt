package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido

/**
 * Diálogo de confirmación para eliminar una sesión de revisión del historial.
 *
 * Es un diálogo modal que no puede cerrarse tocando fuera de él, requiriendo
 * una acción explícita del usuario. Advierte que la operación es irreversible,
 * dado que la eliminación de una sesión en Room elimina también todos los
 * productos revisados asociados por CASCADE.
 *
 * @param operador Nombre del auxiliar responsable de la sesión a eliminar,
 * mostrado en el cuerpo del diálogo para identificar la sesión afectada.
 * @param onConfirmar Callback invocado al pulsar el botón de confirmación.
 * @param onCancelar Callback invocado al pulsar el botón de cancelación.
 */
@Composable
fun DialogConfirmarEliminarSesion(
    operador: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        containerColor   = ColorCard,
        title = {
            Text(
                text       = stringResource(R.string.eliminar_sesion),
                color      = ColorTexto,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text       = "${stringResource(R.string.confirmar_aliminar_sesion)} ($operador)",
                color      = ColorDim,
                fontFamily = FontFamily.Monospace,
                fontSize   = 13.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text(
                    text       = stringResource(R.string.texto_boton_eliminar),
                    color      = ColorVencido,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 13.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text(
                    text       = stringResource(R.string.texto_cancelar),
                    color      = ColorDim,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    )
}