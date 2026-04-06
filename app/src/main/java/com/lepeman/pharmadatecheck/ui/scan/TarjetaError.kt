package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido

/**
 * Tarjeta de error para la pantalla de escaneo.
 *
 * Se muestra cuando [ScanViewModel] expone el estado
 * [ScanViewModel.ScanUiState.ProductoNoEncontrado] o
 * [ScanViewModel.ScanUiState.Error], presentando el mensaje con borde
 * rojo translúcido y texto en [ColorVencido] para mantener la codificación
 * cromática del sistema.
 *
 * @param mensaje Descripción del error a mostrar al usuario.
 */
@Composable
fun TarjetaError(mensaje: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(12.dp),
        border   = BorderStroke(1.dp, ColorVencido.copy(alpha = 0.5f))
    ) {
        Text(
            text       = mensaje,
            color      = ColorVencido,
            fontSize   = 13.sp,
            fontFamily = FontFamily.Monospace,
            modifier   = Modifier.padding(16.dp),
            textAlign  = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun TarjetaErrorPreview() {
    TarjetaError("Producto no encontrado en la base de datos.\nVerifique el código EAN-13.")
}