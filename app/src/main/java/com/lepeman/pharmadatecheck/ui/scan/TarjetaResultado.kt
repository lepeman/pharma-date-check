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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, ColorVigente.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Badge de clasificación
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(color.copy(alpha = 0.2f))
                    .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = resultado.clasificacion.name,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            HorizontalDivider(color = ColorBorde)

            // Datos del producto
            FilaDato("Producto", resultado.producto.nombre)
            FilaDato("Laboratorio", resultado.nombreLaboratorio)
            FilaDato("EAN-13", resultado.producto.codigoEAN13)
            FilaDato("Vencimiento", resultado.fechaVencimiento.format(formatterMesAnio))

            resultado.fechaLimiteCanje?.let { limite ->
                FilaDato(
                    etiqueta = "Límite canje",
                    valor = limite.format(formatterMesAnio),
                    colorValor = ColorCanjeable
                )
            }

            val tiempoRestante = when {
                resultado.diasRestantes <= 0    -> "Vencido"
                resultado.diasRestantes <= 30   -> "${resultado.diasRestantes} días"
                else                            -> "${resultado.diasRestantes / 30} meses"
            }

            FilaDato(
                etiqueta = stringResource(R.string.tiempo_restante),
                valor = tiempoRestante
            )

            HorizontalDivider(color = ColorBorde)

            // Botón nuevo escaneo
            Button(
                onClick = onNuevoEscaneo,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorVigente,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.nuevo_escaneo),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun TarjetaResultadoPreview() {
    PharmaDateCheckTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ejemplo: Producto VIGENTE
            TarjetaResultado(
                resultado = ResultadoClasificacion(
                    producto = Producto("7800000000012", "Paracetamol 500mg", 1),
                    nombreLaboratorio = "Chile",
                    fechaVencimiento = LocalDate.now().plusMonths(24),
                    clasificacion = Clasificacion.VIGENTE,
                    diasRestantes = 720,
                    fechaLimiteCanje = null // Requerido por el constructor
                ),
                onNuevoEscaneo = {}
            )
        }
    }
}