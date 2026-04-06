package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVencido
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import java.time.LocalDate

/**
 * Diálogo modal para confirmar la fecha de vencimiento de un producto escaneado.
 *
 * Solicita al auxiliar el día, mes y año de vencimiento mediante tres [CampoFecha]
 * numéricos. Al confirmar, intenta construir un [LocalDate] con los valores
 * ingresados: si la fecha es válida invoca [onConfirmar]; si no, muestra un
 * mensaje de error en [ColorVencido] sin cerrar el diálogo.
 *
 * El diálogo puede cerrarse mediante el botón Cancelar, que invoca [onCancelar]
 * sin producir ninguna clasificación.
 *
 * @param onConfirmar Callback invocado con la [LocalDate] construida a partir
 * de los valores ingresados cuando la fecha es válida.
 * @param onCancelar Callback invocado al pulsar el botón Cancelar.
 */
@Composable
fun DialogFechaVencimiento(
    onConfirmar: (LocalDate) -> Unit,
    onCancelar: () -> Unit
) {
    var dia        by remember { mutableStateOf("") }
    var mes        by remember { mutableStateOf("") }
    var anio       by remember { mutableStateOf("") }
    var errorFecha by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onCancelar) {
        Card(
            colors = CardDefaults.cardColors(containerColor = ColorCard),
            shape  = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text       = stringResource(R.string.campo_fecha_vencimiento),
                    color      = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text       = stringResource(R.string.indicaciones),
                    color      = ColorDim,
                    fontSize   = 12.sp,
                    fontFamily = FontFamily.Monospace
                )

                // Campos de día, mes y año
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CampoFecha("DD",   dia,  2, Modifier.weight(1f)) { dia  = it }
                    CampoFecha("MM",   mes,  2, Modifier.weight(1f)) { mes  = it }
                    CampoFecha("AAAA", anio, 4, Modifier.weight(2f)) { anio = it }
                }

                // Mensaje de error de validación
                errorFecha?.let {
                    Text(
                        text       = it,
                        color      = ColorVencido,
                        fontSize   = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Botones de acción
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier              = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick  = onCancelar,
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = ColorDim)
                    ) {
                        Text("Cancelar", fontFamily = FontFamily.Monospace)
                    }
                    Button(
                        onClick = {
                            try {
                                val fecha = LocalDate.of(
                                    anio.toInt(),
                                    mes.toInt(),
                                    dia.toInt()
                                )
                                errorFecha = null
                                onConfirmar(fecha)
                            } catch (e: Exception) {
                                errorFecha = "Fecha inválida. Verifique los valores."
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = ColorVigente,
                            contentColor   = Color.White
                        )
                    ) {
                        Text("Confirmar", fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogFechaVencimientoPreview() {
    DialogFechaVencimiento(
        onConfirmar = {},
        onCancelar  = {}
    )
}