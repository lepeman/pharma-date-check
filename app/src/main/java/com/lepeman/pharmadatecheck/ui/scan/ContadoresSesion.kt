package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorCanjeable
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

/**
 * Panel superior de la pantalla de escaneo que muestra los contadores
 * acumulados de la sesión activa y el nombre del auxiliar autenticado.
 *
 * Presenta tres [BadgeContador] con los totales de productos VIGENTES (V),
 * CANJEABLES (C) y VENCIDOS (X), actualizados en tiempo real a medida que
 * el auxiliar procesa productos durante la sesión. Incluye el botón de
 * cierre de sesión que invoca [onCerrarSesion].
 *
 * @param nombreOperador Nombre completo del auxiliar autenticado, mostrado
 * en la cabecera del panel.
 * @param vigentes Cantidad acumulada de productos clasificados como VIGENTE.
 * @param canjeables Cantidad acumulada de productos clasificados como CANJEABLE.
 * @param vencidos Cantidad acumulada de productos clasificados como VENCIDO.
 * @param onCerrarSesion Callback invocado al pulsar el botón "Cerrar sesión".
 */
@Composable
fun ContadoresSesion(
    nombreOperador: String,
    vigentes: Int,
    canjeables: Int,
    vencidos: Int,
    onCerrarSesion: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorVigente,
            contentColor   = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Nombre del auxiliar autenticado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text       = nombreOperador,
                    fontFamily = FontFamily.Monospace,
                    modifier   = Modifier.align(Alignment.Center)
                )
            }

            // Contadores y botón de cierre de sesión
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    BadgeContador("V", vigentes,   ColorVigente)
                    BadgeContador("C", canjeables, ColorCanjeable)
                    BadgeContador("X", vencidos,   ColorVencido)
                }
                TextButton(onClick = onCerrarSesion) {
                    Text(
                        text     = "Cerrar sesión",
                        color    = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContadoresSesionPreview() {
    PharmaDateCheckTheme {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            ContadoresSesion(
                nombreOperador = "Luis Andrés Ortega Lepe",
                vigentes       = 12,
                canjeables     = 5,
                vencidos       = 2,
                onCerrarSesion = {}
            )
        }
    }
}