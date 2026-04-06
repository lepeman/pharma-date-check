package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido

/**
 * Tarjeta de error para la pantalla de gestión de políticas de canje.
 *
 * Se muestra cuando [CanjeViewModel] expone el estado [CanjeViewModel.UiState.Error],
 * presentando el mensaje de error con fondo rojo translúcido y texto en
 * [ColorVencido] para mantener la codificación cromática del sistema.
 *
 * @param mensaje Descripción del error a mostrar al usuario.
 */
@Composable
fun TarjetaErrorCanje(mensaje: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = ColorVencido.copy(alpha = 0.15f)),
        shape    = RoundedCornerShape(12.dp)
    ) {
        Text(
            text       = mensaje,
            color      = ColorVencido,
            fontSize   = 13.sp,
            fontFamily = FontFamily.Monospace,
            modifier   = Modifier.padding(16.dp)
        )
    }
}