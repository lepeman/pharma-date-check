package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido

/**
 * Diálogo de confirmación para eliminar una política de canje.
 *
 * Solicita confirmación explícita antes de ejecutar la eliminación,
 * advirtiendo al usuario que la acción es irreversible. Al confirmar,
 * [CanjeViewModel] elimina la política de la base de datos junto con
 * sus datos asociados por CASCADE.
 *
 * @param laboratorio Identificador del laboratorio cuya política se eliminará,
 * mostrado en el cuerpo del diálogo para identificar la política afectada.
 * @param onConfirmar Callback invocado al pulsar el botón "Eliminar".
 * @param onCancelar Callback invocado al pulsar "Cancelar" o al cerrar el diálogo.
 */
@Composable
fun DialogConfirmarEliminar(
    laboratorio: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        containerColor = ColorCard,
        title = {
            Text(
                text       = "Eliminar política",
                color      = ColorTexto,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text       = "¿Eliminar la política de canje de $laboratorio? Esta acción no se puede deshacer.",
                color      = ColorDim,
                fontFamily = FontFamily.Monospace,
                fontSize   = 13.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text(
                    text       = "Eliminar",
                    color      = ColorVencido,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text(
                    text       = "Cancelar",
                    color      = ColorDim,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    )
}