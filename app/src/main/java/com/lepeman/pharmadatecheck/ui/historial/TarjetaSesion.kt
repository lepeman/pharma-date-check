package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCanjeable
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import java.time.format.DateTimeFormatter

/**
 * Tarjeta que representa una sesión de revisión en la lista del historial.
 *
 * Muestra el nombre del auxiliar responsable, la fecha y hora de inicio,
 * los contadores de productos clasificados por categoría y la fecha de
 * cierre si la sesión fue cerrada formalmente. Al tocar la tarjeta se
 * navega al detalle de la sesión; el ícono de papelera abre el diálogo
 * de confirmación de eliminación.
 *
 * @param sesion Sesión de revisión a mostrar.
 * @param nombreAuxiliar Nombre del auxiliar responsable, resuelto previamente
 * por [HistorialViewModel] a partir del [SesionRevision.auxiliarId].
 * @param onClick Callback invocado al tocar la tarjeta para ver el detalle.
 * @param onEliminar Callback invocado al pulsar el ícono de eliminación.
 */
@Composable
fun TarjetaSesion(
    sesion: SesionRevision,
    nombreAuxiliar: String,
    onClick: () -> Unit,
    onEliminar: () -> Unit
) {
    val formatterDT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val total = sesion.totalVigentes + sesion.totalCanjeables + sesion.totalVencidos

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape  = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // Cabecera: nombre del auxiliar, fecha de inicio e ícono de eliminación
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text       = nombreAuxiliar,
                        color      = ColorTexto,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text       = sesion.fechaInicio.format(formatterDT),
                        color      = ColorDim,
                        fontSize   = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector        = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.texto_boton_eliminar),
                        tint               = ColorVencido
                    )
                }
            }

            HorizontalDivider(
                color    = ColorBorde,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            // Contadores por categoría de clasificación
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ChipResumen(stringResource(R.string.texto_vigentes),   sesion.totalVigentes,   ColorVigente)
                ChipResumen(stringResource(R.string.texto_canjeables), sesion.totalCanjeables, ColorCanjeable)
                ChipResumen(stringResource(R.string.texto_vencidos),   sesion.totalVencidos,   ColorVencido)
                ChipResumen(stringResource(R.string.texto_total),      total,                  ColorTexto)
            }

            // Fecha de cierre si la sesión fue cerrada formalmente
            sesion.fechaTermino?.let { fin ->
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text       = "Cerrada: ${fin.format(formatterDT)}",
                    color      = ColorDim,
                    fontSize   = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}