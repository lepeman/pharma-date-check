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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import com.lepeman.pharmadatecheck.ui.viewmodels.CanjeViewModel

/**
 * Diálogo modal para crear o editar una política de canje.
 *
 * Muestra un formulario con campos de autocompletado para razón social y
 * laboratorio, un interruptor para habilitar el canje por vencimiento y
 * tres campos de mes para el período de canje activo. El título del diálogo
 * cambia entre "Nueva política" y "Editar política" según el estado de [form].
 *
 * Los campos de mes solo son editables cuando el interruptor de vencimiento
 * está activo, comportamiento gestionado por [SwitchVencimientoFormulario]
 * y [CampoFormulario] mediante el parámetro `enabled`.
 *
 * @param form Estado visible del formulario con los valores actuales.
 * @param sugerenciasEmpresas Lista de empresas sugeridas para el autocompletado.
 * @param expandedEmpresa Estado de expansión del menú de empresas.
 * @param onEmpresaChange Callback al cambiar el texto del campo de empresa.
 * @param onEmpresaSeleccionada Callback al seleccionar una empresa del menú.
 * @param onExpandedEmpresaChange Callback al cambiar el estado del menú de empresas.
 * @param sugerenciasLaboratorios Lista de laboratorios sugeridos para el autocompletado.
 * @param expandedLaboratorio Estado de expansión del menú de laboratorios.
 * @param onLaboratorioChange Callback al cambiar el texto del campo de laboratorio.
 * @param onLaboratorioSeleccionado Callback al seleccionar un laboratorio del menú.
 * @param onExpandedLaboratorioChange Callback al cambiar el estado del menú de laboratorios.
 * @param textoMesUno Texto crudo del campo del primer mes del período de canje.
 * @param textoMesDos Texto crudo del campo del segundo mes del período de canje.
 * @param textoMesTres Texto crudo del campo del tercer mes del período de canje.
 * @param onVencimientoChange Callback al cambiar el estado del interruptor de vencimiento.
 * @param onMesUnoChange Callback al cambiar el texto del primer mes.
 * @param onMesDosChange Callback al cambiar el texto del segundo mes.
 * @param onMesTresChange Callback al cambiar el texto del tercer mes.
 * @param onGuardar Callback invocado al pulsar el botón "Guardar".
 * @param onCancelar Callback invocado al pulsar "Cancelar" o cerrar el diálogo.
 */
@Composable
fun DialogFormularioCanje(
    form: CanjeViewModel.FormularioState.Visible,
    sugerenciasEmpresas: List<Empresa>,
    expandedEmpresa: Boolean,
    onEmpresaChange: (String) -> Unit,
    onEmpresaSeleccionada: (Empresa) -> Unit,
    onExpandedEmpresaChange: (Boolean) -> Unit,
    sugerenciasLaboratorios: List<Laboratorio>,
    expandedLaboratorio: Boolean,
    onLaboratorioChange: (String) -> Unit,
    onLaboratorioSeleccionado: (Laboratorio) -> Unit,
    onExpandedLaboratorioChange: (Boolean) -> Unit,
    textoMesUno: String,
    textoMesDos: String,
    textoMesTres: String,
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
                // Título dinámico según modo creación o edición
                Text(
                    text = if (form.politica == null)
                        stringResource(R.string.nueva_politica)
                    else
                        stringResource(R.string.editar_politica),
                    color      = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 15.sp,
                    fontFamily = FontFamily.Monospace
                )

                // Campo de razón social con autocompletado
                CampoBusquedaFormulario(
                    consulta         = form.razonSocial,
                    sugerencias      = sugerenciasEmpresas,
                    expanded         = expandedEmpresa,
                    onQueryChange    = onEmpresaChange,
                    onOptionSelected = onEmpresaSeleccionada,
                    onExpandedChange = onExpandedEmpresaChange,
                    itemLabel        = { it.razonSocial },
                    label            = "Razón Social"
                )

                // Campo de laboratorio con autocompletado
                CampoBusquedaFormulario(
                    consulta         = form.laboratorio,
                    sugerencias      = sugerenciasLaboratorios,
                    expanded         = expandedLaboratorio,
                    onQueryChange    = onLaboratorioChange,
                    onOptionSelected = onLaboratorioSeleccionado,
                    onExpandedChange = onExpandedLaboratorioChange,
                    itemLabel        = { it.nombre },
                    label            = "Laboratorio"
                )

                // Interruptor de política de canje por vencimiento
                SwitchVencimientoFormulario(
                    checked        = form.vencimiento,
                    onCheckedChange = onVencimientoChange
                )

                // Campos de meses del período de canje activo
                CampoFormulario(
                    label        = "Mes 1",
                    value        = textoMesUno,
                    onValueChange = onMesUnoChange,
                    placeholder  = "Ej: 06/2026",
                    singleLine   = false
                )
                CampoFormulario(
                    label        = "Mes 2",
                    value        = textoMesDos,
                    onValueChange = onMesDosChange,
                    placeholder  = "Ej: 07/2026",
                    singleLine   = false
                )
                CampoFormulario(
                    label        = "Mes 3",
                    value        = textoMesTres,
                    onValueChange = onMesTresChange,
                    placeholder  = "Ej: 08/2026",
                    singleLine   = false
                )

                // Botones de acción
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick  = onCancelar,
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = ColorDim)
                    ) {
                        Text("Cancelar", fontFamily = FontFamily.Monospace)
                    }
                    Button(
                        onClick  = onGuardar,
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = ColorVigente,
                            contentColor   = Color.White
                        )
                    ) {
                        Text("Guardar", fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}