package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import java.time.format.DateTimeFormatter

@Composable
fun PantallaDetalleSesion(
    sesion: SesionRevision,
    productos: List<ProductoRevisado>,
    filtroActivo: String?,
    onFiltroChange: (String?) -> Unit,
    onVolver: () -> Unit
) {
    val formatterDT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val formatterF  = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Cabecera con botón volver
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(
                    Icons.Default.ArrowBack, contentDescription = "Volver",
                    tint = ColorTexto)
            }
            Column {
                Text(
                    text = sesion.auxiliarId.toString(),
                    color = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = sesion.fechaInicio.format(formatterDT),
                    color = ColorDim,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Divider(color = ColorBorde)

        // Filtros por clasificación
        val opciones = listOf(null, "VIGENTE", "CANJEABLE", "VENCIDO")
        val etiquetas = listOf("Todos", "Vigentes", "Canjeables", "Vencidos")

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(opciones.size) { i ->
                val activo = filtroActivo == opciones[i]
                FilterChip(
                    selected = activo,
                    onClick = { onFiltroChange(opciones[i]) },
                    label = {
                        Text(
                            etiquetas[i],
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ColorBorde,
                        selectedLabelColor = ColorTexto,
                        labelColor = ColorDim
                    )
                )
            }
        }

        // Lista de productos
        if (productos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No hay productos con este filtro.",
                    color = ColorDim,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(productos, key = { it.id }) { producto ->
                    TarjetaProductoRevisado(
                        producto = producto,
                        formatterF = formatterF,
                        formatterDT = formatterDT
                    )
                }
            }
        }
    }
}