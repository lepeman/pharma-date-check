package com.lepeman.pharmadatecheck.ui.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.medicheck.data.repository.LaboratorioRepository
import com.lepeman.medicheck.data.repository.PoliticaCanjeRepository
import com.lepeman.medicheck.data.repository.ProductoRepository
import com.lepeman.medicheck.data.repository.ProductoRevisadoRepository
import com.lepeman.medicheck.data.repository.SesionRevisionRepository
import com.lepeman.medicheck.ui.theme.ColorBorde
import com.lepeman.medicheck.ui.theme.ColorCard
import com.lepeman.medicheck.ui.theme.ColorDim
import com.lepeman.medicheck.ui.theme.ColorTexto
import com.lepeman.medicheck.ui.theme.ColorVencido
import com.lepeman.medicheck.ui.theme.ColorVigente
import com.lepeman.medicheck.ui.viewmodel.ConfigViewModel
import com.lepeman.medicheck.ui.viewmodel.MedicheckViewModelFactory

// ── Componible: tarjeta de importación reutilizable ──────────────────────────
@Composable
fun TarjetaImportacion(
    titulo: String,
    descripcionFormato: String,
    notaOmitidos: String,
    importState: ConfigViewModel.ImportState,
    textoBoton: String,
    onSeleccionar: () -> Unit,
    onReintentar: () -> Unit,
    onResetear: () -> Unit,
    textoExito: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ColorCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = titulo,
                color = ColorTexto,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = descripcionFormato,
                color = ColorDim,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 16.sp
            )

            HorizontalDivider(color = ColorBorde)

            when (val state = importState) {
                is ConfigViewModel.ImportState.Inactivo -> {
                    Button(
                        onClick = onSeleccionar,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorBorde,
                            contentColor = ColorTexto
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            textoBoton,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                is ConfigViewModel.ImportState.Procesando -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = ColorTexto,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Procesando archivo...",
                            color = ColorDim,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                is ConfigViewModel.ImportState.Exito -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = ColorVigente.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Importación completada",
                                color = ColorVigente,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "✓ ${state.importados} $textoExito",
                                color = ColorVigente,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            if (state.omitidos > 0) {
                                Text(
                                    text = "— ${state.omitidos} omitidos (ya existían o formato inválido)",
                                    color = ColorDim,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = onResetear,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ColorDim
                        )
                    ) {
                        Text(
                            "Importar otro archivo",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                    }
                }

                is ConfigViewModel.ImportState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = ColorVencido.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = state.mensaje,
                            color = ColorVencido,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Start
                        )
                    }
                    Button(
                        onClick = onReintentar,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorBorde,
                            contentColor = ColorTexto
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Reintentar",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = notaOmitidos,
                color = ColorDim,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun TarjetaImportacionPreview() {
    TarjetaImportacion(
        titulo = "Importar Productos",
        descripcionFormato = "Formato esperado:\n" +
                "codigoEAN13,nombre,laboratorio\n" +
                "7802000000001,Paracetamol 500mg,Bagó",
        notaOmitidos = "Los productos con EAN-13 ya registrados serán omitidos.",
        importState = ConfigViewModel.ImportState.Procesando,
        textoBoton = "Seleccionar CSV de productos",
        onSeleccionar = {},
        onReintentar = {},
        onResetear = {},
        textoExito = "Productos agregados"
    )
}