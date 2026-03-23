package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCanjeable
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme
import java.time.LocalDate

@Composable
fun TarjetaPoliticaCanje(
    politica: PoliticaCanje,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Laboratorio ${politica.laboratorioId}",
                    color = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
                Row {
                    IconButton(onClick = onEditar) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = ColorCanjeable
                        )
                    }
                    IconButton(onClick = onEliminar) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = ColorVencido
                        )
                    }
                }
            }

            HorizontalDivider(color = ColorBorde, modifier = Modifier.padding(vertical = 6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DatoCanje("Anticipación", "10 días")
                //DatoCanje("Recuperación", "${politica.porcentajeRecuperacion.toInt()}%")
                DatoCanje("Recuperación", "30%")
            }

//            politica.condiciones?.let { condiciones ->
//                if (condiciones.isNotBlank()) {
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        text = condiciones,
//                        color = ColorDim,
//                        fontSize = 11.sp,
//                        fontFamily = FontFamily.Monospace
//                    )
//                }
//            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun TarjetaPoliticaCanjePreview() {
    PharmaDateCheckTheme {
        TarjetaPoliticaCanje(
            politica = PoliticaCanje(
                id = 0,
                empresaId = 2,
                laboratorioId = 2,
                vencimiento = true,
                mesUno = LocalDate.parse("2026-03-10"),
                mesDos = LocalDate.parse("2026-04-10"),
                mesTres = LocalDate.parse("2026-05-10")
            ),
            onEditar = {},
            onEliminar = {}
        )
    }
}
