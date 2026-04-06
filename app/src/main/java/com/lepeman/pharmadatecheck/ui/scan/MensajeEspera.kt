package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorDim

/**
 * Mensaje de espera mostrado en la pantalla de escaneo cuando no hay
 * ningún producto clasificado aún en la sesión activa.
 *
 * Ocupa el área del resultado en [ScanScreen] mientras el auxiliar no ha
 * procesado ningún producto, indicando que la aplicación está lista para
 * recibir un escaneo HID o una entrada manual.
 */
@Composable
fun MensajeEspera() {
    Box(
        modifier         = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = "Esperando escaneo...",
            color      = ColorDim,
            fontSize   = 14.sp,
            fontFamily = FontFamily.Monospace,
            textAlign  = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun MensajeEsperaPreview() {
    MensajeEspera()
}