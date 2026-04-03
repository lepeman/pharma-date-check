package com.lepeman.pharmadatecheck.ui.config

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.ui.PharmaBottomAppBar
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.viewmodels.ConfigViewModel

@Composable
fun ConfigScreenContent(
    configSelected: Boolean,
    totalProductos: Int,
    totalLaboratorios: Int,
    launcherStateProductos: ManagedActivityResultLauncher<String, Uri?>,
    launcherStateLaboratorios:  ManagedActivityResultLauncher<String, Uri?>,
    importStateProductos: ConfigViewModel.ImportState,
    importStateLaboratorios: ConfigViewModel.ImportState,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit,
    onResetearEstadoProductos: () -> Unit,
    onResetearEstadoLaboratorios: () -> Unit
) {

    Scaffold(
        bottomBar = {
            PharmaBottomAppBar(
                configSelected = configSelected,
                navigateToScan = navigateToScan,
                navigateToHistorial = navigateToHistorial,
                navigateToCanje = navigateToCanje,
                navigateToConfig = navigateToConfig
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorFondo)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(bottom = 24.dp), // padding extra para que el último elemento no quede pegado a elementos inferiores.
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Configuración",
                color = ColorTexto,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "Gestione el catálogo de productos y laboratorios",
                color = ColorDim,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )

            HorizontalDivider(color = ColorBorde)

            // Tarjeta: estado del catálogo
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Catálogo actual",
                        color = ColorDim,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DatosCatalogo(
                            cantidad = totalProductos,
                            etiqueta = "productos"
                        )
                        DatosCatalogo(
                            cantidad = totalLaboratorios,
                            etiqueta = "laboratorios"
                        )
                    }
                }
            }

            TarjetaImportacion(
                titulo = "Importar productos",
                descripcionFormato = "Formato esperado:\n" +
                        "CODIGO EAN13, NOMBRE, COD LABORATORIO\n" +
                        "7802000000001, PARACETAMOL 500MG, 405",
                notaOmitidos = "Los laboratorios con EAN 13 ya registrado serán omitidos",
                importState = importStateProductos,
                textoBoton = "Seleccionar CSV de productos",
                onSeleccionar = { launcherStateProductos.launch("text/*") },
                onReintentar = { launcherStateProductos.launch("text/*") },
                onResetear = onResetearEstadoProductos,
                textoExito = "productos agregados"
            )

            TarjetaImportacion(
                titulo = "Importar Laboratorios",
                descripcionFormato = "Formato esperado:\n" +
                        "CODIGO LABORATORIO, LABORATORIO\n" +
                        "14, MENTHOLATUM",
                notaOmitidos = "Los laboratorios con código ya registrado serán omitidos",
                importState = importStateLaboratorios,
                textoBoton = "Seleccionar CSV de laboratorios",
                onSeleccionar = { launcherStateLaboratorios.launch("text/*") },
                onReintentar = { launcherStateLaboratorios.launch("text/*") },
                onResetear = onResetearEstadoLaboratorios,
                textoExito = "laboratorios agregados"
            )
        }
    }
}