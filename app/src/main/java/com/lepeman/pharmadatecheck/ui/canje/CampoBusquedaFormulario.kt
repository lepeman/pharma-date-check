package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.viewmodels.CanjeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CampoBusquedaFormulario(
    consulta: String,
    sugerencias: List<T>,
    expanded: Boolean,
    onQueryChange: (String) -> Unit,
    onOptionSelected: (T) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    itemLabel: (T) -> String,
    label: String = "Buscar...",
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = consulta,
            onValueChange = onQueryChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ColorBorde.copy(alpha = 0.4f),
                unfocusedBorderColor = ColorBorde,
                focusedTextColor = ColorTexto,
                unfocusedTextColor = ColorTexto,
                cursorColor = ColorTexto
            )
        )

        if (expanded && sugerencias.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                sugerencias.forEach { opcion ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                itemLabel(opcion),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = ColorTexto
                            )
                        },
                        onClick = {
                            onOptionSelected(opcion)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}