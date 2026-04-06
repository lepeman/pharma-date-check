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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import com.lepeman.pharmadatecheck.ui.viewmodels.ConfigViewModel

/**
 * Tarjeta de importación de datos desde archivo CSV, reutilizable para
 * productos y laboratorios.
 *
 * Adapta su contenido según el estado actual de [importState]:
 * - [ConfigViewModel.ImportState.Inactivo]: muestra el botón de selección de archivo.
 * - [ConfigViewModel.ImportState.Procesando]: muestra un indicador de progreso.
 * - [ConfigViewModel.ImportState.Exito]: muestra el resumen de registros importados
 *   y omitidos, con opción de importar otro archivo.
 * - [ConfigViewModel.ImportState.Error]: muestra el mensaje de error y un botón
 *   de reintento.
 *
 * @param titulo Título descriptivo de la tarjeta.
 * @param descripcionFormato Descripción del formato esperado del archivo CSV.
 * @param notaOmitidos Nota informativa sobre el criterio de omisión de registros.
 * @param importState Estado actual de la operación de importación.
 * @param textoBoton Texto del botón principal de selección de archivo.
 * @param onSeleccionar Callback invocado al pulsar el botón de selección.
 * @param onReintentar Callback invocado al pulsar el botón de reintento en estado Error.
 * @param onResetear Callback invocado al pulsar "Importar otro archivo" en estado Exito.
 * @param textoExito Texto descriptivo del tipo de registros importados,
 * mostrado junto al contador en el estado Exito (por ejemplo, "productos agregados").
 */
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
        colors   = CardDefaults.cardColors(containerColor = ColorCard),
        shape    = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text       = titulo,
                color      = ColorTexto,
                fontWeight = FontWeight.Bold,
                fontSize   = 14.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text       = descripcionFormato,
                color      = ColorDim,
                fontSize   = 11.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 16.sp
            )

            HorizontalDivider(color = ColorBorde)

            when (val state = importState) {

                // Estado inicial: botón de selección de archivo
                is ConfigViewModel.ImportState.Inactivo -> {
                    Button(
                        onClick  = onSeleccionar,
                        modifier = Modifier.fillMaxWidth(),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = ColorVigente,
                            contentColor   = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text       = textoBoton,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Estado de procesamiento: indicador de progreso
                is ConfigViewModel.ImportState.Procesando -> {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier    = Modifier.size(20.dp),
                            color       = ColorVigente,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text       = "Procesando archivo...",
                            color      = ColorDim,
                            fontSize   = 13.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Estado de éxito: resumen de importación
                is ConfigViewModel.ImportState.Exito -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors   = CardDefaults.cardColors(
                            containerColor = ColorVigente.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text       = "Importación completada",
                                color      = ColorVigente,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 13.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text       = "✓ ${state.importados} $textoExito",
                                color      = ColorVigente,
                                fontSize   = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            if (state.omitidos > 0) {
                                Text(
                                    text       = "— ${state.omitidos} omitidos (ya existían o formato inválido)",
                                    color      = ColorDim,
                                    fontSize   = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                    OutlinedButton(
                        onClick  = onResetear,
                        modifier = Modifier.fillMaxWidth(),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = ColorDim)
                    ) {
                        Text(
                            text       = "Importar otro archivo",
                            fontFamily = FontFamily.Monospace,
                            fontSize   = 13.sp
                        )
                    }
                }

                // Estado de error: mensaje y botón de reintento
                is ConfigViewModel.ImportState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors   = CardDefaults.cardColors(
                            containerColor = ColorVencido.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text       = state.mensaje,
                            color      = ColorVencido,
                            fontSize   = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier   = Modifier.padding(12.dp),
                            textAlign  = TextAlign.Start
                        )
                    }
                    Button(
                        onClick  = onReintentar,
                        modifier = Modifier.fillMaxWidth(),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = ColorVigente,
                            contentColor   = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text       = "Reintentar",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text      = notaOmitidos,
                color     = ColorDim,
                fontSize  = 11.sp,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun TarjetaImportacionPreview() {
    TarjetaImportacion(
        titulo             = "Importar Productos",
        descripcionFormato = "Formato esperado:\n" +
                "codigoEAN13,nombre,laboratorio\n" +
                "7802000000001,Paracetamol 500mg,Bagó",
        notaOmitidos       = "Los productos con EAN-13 ya registrados serán omitidos.",
        importState        = ConfigViewModel.ImportState.Procesando,
        textoBoton         = "Seleccionar CSV de productos",
        onSeleccionar      = {},
        onReintentar       = {},
        onResetear         = {},
        textoExito         = "productos agregados"
    )
}