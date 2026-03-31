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
import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.viewmodels.CanjeViewModel
import com.lepeman.pharmadatecheck.ui.viewmodels.CanjeViewModel.FormularioState
import java.time.LocalDate

@Composable
fun DialogFormularioCanje(
    form: CanjeViewModel.FormularioState.Visible,
    // Empresa
    sugerenciasEmpresas: List<Empresa>,
    expandedEmpresa: Boolean,
    onEmpresaChange: (String) -> Unit,
    onEmpresaSeleccionada: (Empresa) -> Unit,
    onExpandedEmpresaChange: (Boolean) -> Unit,
    // Laboratorio
    sugerenciasLaboratorios: List<Laboratorio>,
    expandedLaboratorio: Boolean,
    onLaboratorioChange: (String) -> Unit,
    onLaboratorioSeleccionado: (Laboratorio) -> Unit,
    onExpandedLaboratorioChange: (Boolean) -> Unit,
    // Fechas
    textoMesUno: String,
    textoMesDos: String,
    textoMesTres: String,
    // Resto
    onVencimientoChange: (Boolean) -> Unit,
    onMesUnoChange: (String) -> Unit,
    onMesDosChange: (String) -> Unit,
    onMesTresChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
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
                    if (form.politica == null) stringResource(R.string.nueva_politica) else stringResource(R.string.editar_politica),
                    color = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace
                )

                // Campo Razón Social
                CampoBusquedaFormulario(
                    consulta = form.razonSocial,
                    sugerencias = sugerenciasEmpresas,
                    expanded = expandedEmpresa,
                    onQueryChange = onEmpresaChange,
                    onOptionSelected = onEmpresaSeleccionada,
                    onExpandedChange = onExpandedEmpresaChange,
                    itemLabel = { it.razonSocial },
                    label = "Razón Social"
                )

                // Campo Laboratorio
                CampoBusquedaFormulario(
                    consulta = form.laboratorio,
                    sugerencias = sugerenciasLaboratorios,
                    expanded = expandedLaboratorio,
                    onQueryChange = onLaboratorioChange,
                    onOptionSelected = onLaboratorioSeleccionado,
                    onExpandedChange = onExpandedLaboratorioChange,
                    itemLabel = { it.nombre },
                    label = "Laboratorio"
                )

                // Porcentaje de recuperación
                SwitchVencimientoFormulario(
                    checked = form.vencimiento,
                    onCheckedChange = onVencimientoChange
                )

                // Mes Uno
                CampoFormulario(
                    label = "Mes 1",
                    value = textoMesUno,
                    onValueChange = onMesUnoChange,
                    placeholder = "Ej: 06/2026",
                    singleLine = false
                )

                CampoFormulario(
                    label = "Mes 2",
                    value = textoMesDos,
                    onValueChange = onMesDosChange,
                    placeholder = "Ej: 07/2026",
                    singleLine = false
                )

                CampoFormulario(
                    label = "Mes 3",
                    value = textoMesTres,
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