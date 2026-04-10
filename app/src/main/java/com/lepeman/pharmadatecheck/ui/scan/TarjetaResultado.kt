package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import com.lepeman.pharmadatecheck.domain.Clasificacion
import com.lepeman.pharmadatecheck.domain.ResultadoClasificacion
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCanjeable
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Tarjeta que muestra el resultado de la clasificación de un producto escaneado.
 *
 * Presenta un badge con la clasificación asignada ([Clasificacion.VIGENTE],
 * [Clasificacion.CANJEABLE] o [Clasificacion.VENCIDO]) con su color identificador,
 * los datos del producto clasificado mediante [FilaDato], el tiempo restante hasta
 * el vencimiento y la fecha límite de canje cuando aplica. El borde de la tarjeta
 * adopta el color de la clasificación para reforzar la señal visual al auxiliar.
 *
 * @param resultado Resultado de clasificación producido por [ClasificadorProducto].
 * @param onNuevoEscaneo Callback invocado al pulsar el botón "Nuevo escaneo",
 * que restablece el estado de [ScanViewModel] para procesar el siguiente producto.
 */
@Composable
fun TarjetaResultado(
    resultado: ResultadoClasificacion,
    onNuevoEscaneo: () -> Unit
) {
    val color = when (resultado.clasificacion) {
        Clasificacion.VIGENTE   -> ColorVigente
        Clasificacion.CANJEABLE -> ColorCanjeable
        Clasificacion.VENCIDO   -> ColorVencido
    }

    val formatterMesAnio = DateTimeFormatter.ofPattern("MM/yyyy")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(12.dp),
        border   = BorderStroke(1.5.dp, color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Badge de clasificación con color dinámico
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(color.copy(alpha = 0.2f))
                    .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text       = resultado.clasificacion.name,
                    color      = color,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            HorizontalDivider(color = ColorBorde)

            // Datos del producto clasificado
            FilaDato("Producto",    resultado.producto.nombre)
            FilaDato("Laboratorio", resultado.nombreLaboratorio)
            FilaDato("EAN-13",      resultado.producto.codigoEAN13)
            FilaDato("Vencimiento", resultado.fechaVencimiento.format(formatterMesAnio))

            // Fecha límite de canje — solo visible para productos CANJEABLES
            resultado.fechaLimiteCanje?.let { limite ->
                FilaDato(
                    etiqueta   = "Límite canje",
                    valor      = limite.format(formatterMesAnio),
                    colorValor = ColorCanjeable
                )
            }

            // Tiempo restante calculado en días o meses
            val tiempoRestante = when {
                resultado.diasRestantes <= 0  -> "Vencido"
                resultado.diasRestantes <= 30 -> "${resultado.diasRestantes} días"
                else                          -> "${resultado.diasRestantes / 30} meses"
            }

            FilaDato(
                etiqueta = stringResource(R.string.tiempo_restante),
                valor    = tiempoRestante
            )

            HorizontalDivider(color = ColorBorde)

            // Botón para procesar el siguiente producto
            Button(
                onClick  = onNuevoEscaneo,
                modifier = Modifier.fillMaxWidth(),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = ColorVigente,
                    contentColor   = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text       = stringResource(R.string.nuevo_escaneo),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun TarjetaResultadoPreview() {
    PharmaDateCheckTheme {
        Column(
            modifier            = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaResultado(
                resultado = ResultadoClasificacion(
                    producto          = Producto("7800000000012", "Paracetamol 500mg", 1),
                    nombreLaboratorio = "RECALCINE",
                    fechaVencimiento  = LocalDate.now().plusMonths(24),
                    clasificacion     = Clasificacion.VIGENTE,
                    diasRestantes     = 720,
                    fechaLimiteCanje  = null
                ),
                onNuevoEscaneo = {}
            )
        }
    }
}