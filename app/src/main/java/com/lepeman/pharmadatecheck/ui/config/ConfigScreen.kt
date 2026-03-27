package com.lepeman.pharmadatecheck.ui.config

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lepeman.pharmadatecheck.PharmaTopAppBar
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.historial.HistorialDestination
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavigation
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorCard
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.viewmodels.AppViewModelProvider
import com.lepeman.pharmadatecheck.ui.viewmodels.ConfigViewModel
import com.lepeman.pharmadatecheck.ui.viewmodels.ScanViewModel

object ConfigDestination : PharmaNavigation {
    override val route = "config"
    override val titleRes = R.string.config
}


@Composable
fun ConfigScreen(
    navController: NavHostController,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConfigViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination

    val importStateProductos by viewModel.importStateProductos.collectAsState()
    val importStateLaboratorios by viewModel.importStateLaboratorios.collectAsState()
    val totalProductos by viewModel.totalProductos.collectAsState()
    val totalLaboratorios by viewModel.totalLaboratorios.collectAsState()
    val context = LocalContext.current

    // Launcher para productos
    val launcherProductos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.importarProductosCSV(context, it) }
    }

    // Launcher para laboratorios
    val launcherLaboratorios = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.importarLaboratoriosCSV(context, it) }
    }

    Scaffold(
        bottomBar = {
            PharmaTopAppBar(
                configSelected = rutaActual?.hierarchy?.any { it.route == ConfigDestination.route } == true,
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

            // ── Encabezado ────────────────────────────────────────────────────────
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

            // ── Tarjeta: estado del catálogo ──────────────────────────────────────
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

            // ── tarjeta: importación de productos ──────────────────────────────────────────
            TarjetaImportacion(
                titulo = "Importar productos",
                descripcionFormato = "Formato esperado:\n" +
                        "CODIGO EAN13, NOMBRE, COD LABORATORIO\n" +
                        "7802000000001, PARACETAMOL 500MG, 405",
                notaOmitidos = "Los laboratorios con EAN 13 ya registrado serán omitidos",
                importState = importStateProductos,
                textoBoton = "Seleccionar CSV de productos",
                onSeleccionar = { launcherProductos.launch("text/*") },
                onReintentar = { launcherProductos.launch("text/*") },
                onResetear = { viewModel.resetearEstadoProductos() },
                textoExito = "productos agregados"
            )

            // ── Tarjeta: importación de laboratorios ──────────────────────────────────────────
            TarjetaImportacion(
                titulo = "Importar Laboratorios",
                descripcionFormato = "Formato esperado:\n" +
                        "CODIGO LABORATORIO, LABORATORIO\n" +
                        "14, MENTHOLATUM",
                notaOmitidos = "Los laboratorios con código ya registrado serán omitidos",
                importState = importStateLaboratorios,
                textoBoton = "Seleccionar CSV de laboratorios",
                onSeleccionar = { launcherLaboratorios.launch("text/*") },
                onReintentar = { launcherLaboratorios.launch("text/*") },
                onResetear = { viewModel.resetearEstadoLaboratorios() },
                textoExito = "laboratorios agregados"
            )
        }
    }
}