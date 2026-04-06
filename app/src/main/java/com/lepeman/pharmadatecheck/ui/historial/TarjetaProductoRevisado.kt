package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import com.lepeman.pharmadatecheck.ui.theme.ColorCanjeable
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import java.time.format.DateTimeFormatter

/**
 * Tarjeta que representa un producto revisado dentro del detalle de una sesión.
 *
 * Muestra los tres campos disponibles en [ProductoRevisado]: el código EAN-13
 * del producto escaneado, la clasificación asignada por el motor con su color
 * identificador, y el timestamp exacto del escaneo.
 *
 * @param producto Producto revisado a mostrar.
 * @param formatterF Formateador de fecha en formato corto (dd/MM/yyyy), usado
 * para mostrar la fecha del escaneo.
 * @param formatterDT Formateador de fecha y hora (dd/MM/yyyy HH:mm), usado
 * para mostrar el timestamp completo del escaneo.
 */
@Composable
fun TarjetaProductoRevisado(
    producto: ProductoRevisado,
    formatterF: DateTimeFormatter,
    formatterDT: DateTimeFormatter
) {
    val colorClasificacion = when (producto.clasificacion) {
        "VIGENTE"   -> ColorVigente
        "CANJEABLE" -> ColorCanjeable
        else        -> ColorVencido
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Fila principal: código EAN-13 y clasificación
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = producto.codigoEAN13,
                    color      = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier   = Modifier.weight(1f)
                )
                Text(
                    text       = producto.clasificacion,
                    color      = colorClasificacion,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Fila secundaria: fecha y hora del escaneo
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text       = "Escaneado el ${producto.timestamp.format(formatterF)}",
                    color      = ColorDim,
                    fontSize   = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text       = producto.timestamp.format(formatterDT),
                    color      = ColorDim,
                    fontSize   = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}