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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.domain.Clasificacion
import com.lepeman.pharmadatecheck.domain.ResultadoClasificacion
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCanjeable
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
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

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, color.copy(alpha = 0.5f))
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
            FilaDato("Vencimiento", resultado.fechaVencimiento.format(formatter))

            FilaDato(
                "Límite canje",
                resultado.fechaVencimiento.format(formatter),
                colorValor = ColorCanjeable
            )
            FilaDato(
                "Anticipación canje",
                "${resultado.fechaVencimiento} días"
            )

            HorizontalDivider(color = ColorBorde)

            // Botón nuevo escaneo
            Button(
                onClick = onNuevoEscaneo,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorBorde,
                    contentColor = ColorTexto
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Nuevo escaneo",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}