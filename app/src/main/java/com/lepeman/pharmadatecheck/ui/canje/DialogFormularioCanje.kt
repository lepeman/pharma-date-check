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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme
import com.lepeman.pharmadatecheck.ui.viewmodels.CanjeViewModel

@Composable
fun DialogFormularioCanje(
    form: CanjeViewModel.FormularioState.Visible,
    onLaboratorioChange: (String) -> Unit,
    onEmpresaChange: (String) -> Unit,
    onVencimientoChange: (String) -> Unit,
    onMesUnoChange: (String) -> Unit,
    onMesDosChange: (String) -> Unit,
    onMesTresChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    val titulo = if (form.politica == null) "Nueva política" else "Editar política de canje"

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
                    text = titulo,
                    color = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                CampoFormulario(
                    label = "Laboratorio",
                    value = form.nombreLaboratorio,
                    onValueChange = onLaboratorioChange,
                    error = form.errorLaboratorio,
                    placeholder = "Ej: MintLab"
                )

                CampoFormulario(
                    label = "Empresa",
                    value = form.nombreEmpresa,
                    onValueChange = onEmpresaChange,
                    error = form.errorEmpresa,
                    placeholder = "Ej: ANDROMACO S.A."
                )

                CampoFormulario(
                    label = "Vencimiento",
                    value = form.vencimiento,
                    onValueChange = onVencimientoChange,
                    placeholder = "Ej: SI"
                )

                CampoFormulario(
                    label = "Mes Uno",
                    value = form.fechaUno,
                    onValueChange = onMesUnoChange,
                    placeholder = "Ej: 01/2026"
                )

                CampoFormulario(
                    label = "Mes Uno",
                    value = form.fechaUno,
                    onValueChange = onMesDosChange,
                    placeholder = "Ej: 02/2026"
                )

                CampoFormulario(
                    label = "Mes Uno",
                    value = form.fechaUno,
                    onValueChange = onMesTresChange,
                    placeholder = "Ej: 03/2026"
                )

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
                            containerColor = ColorDim,
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

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun DialogFormularioCanjePreview() {
    PharmaDateCheckTheme {
        DialogFormularioCanje(
            form = CanjeViewModel.FormularioState.Visible(),
            onLaboratorioChange = {},
            onEmpresaChange = {},
            onVencimientoChange = {},
            onMesUnoChange = {},
            onMesDosChange = {},
            onMesTresChange = {},
            onGuardar = {},
            onCancelar = {}
        )
    }
}