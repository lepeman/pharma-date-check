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
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente

/**
 * Campo de texto con autocompletado genérico para formularios.
 *
 * Combina un [OutlinedTextField] con un [ExposedDropdownMenuBox] para mostrar
 * sugerencias mientras el usuario escribe. Es genérico sobre el tipo [T], lo que
 * permite reutilizarlo para cualquier entidad del sistema (empresas, laboratorios,
 * etc.) mediante el parámetro [itemLabel] que define cómo se representa cada opción.
 *
 * @param T Tipo de los elementos de la lista de sugerencias.
 * @param consulta Texto actualmente ingresado en el campo.
 * @param sugerencias Lista de opciones a mostrar en el menú desplegable.
 * @param expanded Indica si el menú desplegable está visible.
 * @param onQueryChange Callback invocado al cambiar el texto del campo.
 * @param onOptionSelected Callback invocado al seleccionar una opción del menú.
 * @param onExpandedChange Callback invocado al cambiar el estado de expansión del menú.
 * @param itemLabel Función que convierte un elemento [T] en el texto a mostrar.
 * @param label Etiqueta descriptiva del campo. Por defecto "Buscar...".
 * @param modifier Modificador opcional para personalizar el layout del componente.
 */
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
                focusedBorderColor = ColorVigente,
                unfocusedBorderColor = ColorBorde,
                focusedTextColor = ColorTexto,
                unfocusedTextColor = ColorTexto,
                cursorColor = ColorVigente,
                focusedLabelColor = ColorVigente,
                unfocusedLabelColor = ColorTexto
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
                                text = itemLabel(opcion),
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