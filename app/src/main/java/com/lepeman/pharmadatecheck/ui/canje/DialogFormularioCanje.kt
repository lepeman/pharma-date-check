package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

@Composable
fun DialogFormularioCanje(
    onLaboratorioChange: (String) -> Unit,
    onDiasChange: (String) -> Unit,
    onPorcentajeChange: (String) -> Unit,
    onCondicionesChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1117)
@Composable
fun DialogFormularioCanjePreview() {
    PharmaDateCheckTheme {
        DialogFormularioCanje()
    }
}