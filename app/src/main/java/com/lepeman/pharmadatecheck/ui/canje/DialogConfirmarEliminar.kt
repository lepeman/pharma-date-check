package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

@Composable
fun DialogConfirmarEliminar(
    laboratorioId: Int,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        containerColor = ColorCard,
        title = {
            Text(
                text = "Eliminar política",
                color = ColorTexto,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "¿Eliminar la política de $laboratorioId? Esta acción no se puede deshacer.",
                color = ColorDim,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Eliminar", color = ColorDim, fontFamily = FontFamily.Monospace)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar", color = ColorDim, fontFamily = FontFamily.Monospace)
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun DialogConfirmarEliminarPreview() {
    PharmaDateCheckTheme {
        DialogConfirmarEliminar(
            laboratorioId = 12,
            onConfirmar = {},
            onCancelar = {}
        )
    }
}