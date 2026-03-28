package com.lepeman.pharmadatecheck.ui.canje

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.viewmodels.CanjeViewModel

@Composable
fun DialogFormularioCanje(
    form: CanjeViewModel.FormularioState.Visible,
    onEmpresaChange: (String) -> Unit,
    onLaboratorioChange: (String) -> Unit,
    onVencimientoChange: (Boolean) -> Unit,
    onMesUnoChange: (String) -> Unit,
    onMesDosChange: (String) -> Unit,
    onMesTresChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    val titulo = stringResource(if (form.politica == null) R.string.nueva_politica else R.string.editar_politica)

    Dialog(onDismissRequest = onCancelar) {
        Card(
            colors = CardDefaults.cardColors(containerColor = ColorCard),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    titulo,
                    color = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace
                )

                // Campo laboratorio
                CampoFormulario(
                    label = "RazonSocial",
                    value = form.razonSocial,
                    onValueChange = onEmpresaChange,
                    error = form.errorEmpresa,
                    placeholder = "Ej: Mintlab S.A."
                )

                // Campo Laboratorio
                CampoFormulario(
                    label = "Laboratorio",
                    value = form.laboratorio,
                    onValueChange = onLaboratorioChange,
                    error = form.errorLaboratorio,
                    placeholder = "Ej: MintLab",
                    keyboardType = KeyboardType.Number
                )

                // Porcentaje de recuperación
                SwitchVencimientoFormulario(
                    checked = form.vencimiento,
                    onCheckedChange = onVencimientoChange
                )

                // Mes Uno
                CampoFormulario(
                    label = "Mes 1",
                    value = form.mesUno,
                    onValueChange = onMesUnoChange,
                    placeholder = "Ej: 06/2026",
                    singleLine = false
                )

                CampoFormulario(
                    label = "Mes 2",
                    value = form.mesDos,
                    onValueChange = onMesDosChange,
                    placeholder = "Ej: 07/2026",
                    singleLine = false
                )

                CampoFormulario(
                    label = "Mes 3",
                    value = form.mesTres,
                    onValueChange = onMesTresChange,
                    placeholder = "Ej: 08/2026",
                    singleLine = false
                )

                // Botones
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onCancelar,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorDim)
                    ) {
                        Text("Cancelar", fontFamily = FontFamily.Monospace)
                    }
                    Button(
                        onClick = onGuardar,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorBorde,
                            contentColor = ColorTexto
                        )
                    ) {
                        Text("Guardar", fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}