package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorCanjeable
import com.lepeman.pharmadatecheck.ui.theme.ColorDim

/**
 * Estado vacío de la pantalla de gestión de políticas de canje.
 *
 * Se muestra cuando no hay políticas registradas en la base de datos.
 * Ofrece al usuario la opción de cargar un conjunto de políticas de ejemplo
 * mediante [onPrepoblar], que invoca [CanjeViewModel.prepoblarSiVacio],
 * facilitando las pruebas funcionales sin configuración manual previa.
 *
 * @param onPrepoblar Callback invocado al pulsar el botón de carga de
 * laboratorios frecuentes.
 */
@Composable
fun EstadoVacioCanje(onPrepoblar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text       = "No hay políticas de canje registradas.",
            color      = ColorDim,
            fontSize   = 13.sp,
            fontFamily = FontFamily.Monospace,
            textAlign  = TextAlign.Center
        )
        OutlinedButton(
            onClick = onPrepoblar,
            colors  = ButtonDefaults.outlinedButtonColors(contentColor = ColorCanjeable)
        ) {
            Text(
                text       = "Cargar laboratorios frecuentes",
                fontFamily = FontFamily.Monospace,
                fontSize   = 12.sp
            )
        }
    }
}