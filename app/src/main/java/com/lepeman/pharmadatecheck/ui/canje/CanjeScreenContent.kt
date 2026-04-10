package com.lepeman.pharmadatecheck.ui.canje

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.ui.PharmaBottomAppBar
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorFondo
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.theme.ColorVigente
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme
import com.lepeman.pharmadatecheck.ui.viewmodels.CanjeViewModel
import java.time.LocalDate

/**
 * Contenido stateless de la pantalla de gestión de políticas de canje.
 *
 * Recibe todos los datos y callbacks desde [CanjeScreen] y se encarga
 * únicamente del renderizado. Gestiona los diálogos de formulario y
 * confirmación de eliminación directamente desde este composable.
 */
@Composable
fun CanjeScreenContent(
    canjeSelected: Boolean,
    uiState: CanjeViewModel.UiState,
    formulario: CanjeViewModel.FormularioState,
    politicaAEliminar: PoliticaCanje?,
    sugerenciasEmpresas: List<Empresa>,
    expandedEmpresa: Boolean,
    sugerenciasLaboratorios: List<Laboratorio>,
    expandedLaboratorio: Boolean,
    textoMesUno: String,
    textoMesDos: String,
    textoMesTres: String,
    onNuevaPolitica: () -> Unit,
    onEditarPolitica: (PoliticaCanje) -> Unit,
    onEliminarPolitica: (PoliticaCanje) -> Unit,
    onConfirmarEliminar: () -> Unit,
    onCancelarEliminar: () -> Unit,
    onPrepoblar: () -> Unit,
    onEmpresaChange: (String) -> Unit,
    onEmpresaSeleccionada: (Empresa) -> Unit,
    onExpandedEmpresaChange: (Boolean) -> Unit,
    onLaboratorioChange: (String) -> Unit,
    onLaboratorioSeleccionado: (Laboratorio) -> Unit,
    onExpandedLaboratorioChange: (Boolean) -> Unit,
    onVencimientoChange: (Boolean) -> Unit,
    onMesUnoChange: (String) -> Unit,
    onMesDosChange: (String) -> Unit,
    onMesTresChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCerrarFormulario: () -> Unit,
    navigateToScan: () -> Unit,
    navigateToHistorial: () -> Unit,
    navigateToCanje: () -> Unit,
    navigateToConfig: () -> Unit
) {
    // Diálogo de creación/edición de política
    if (formulario is CanjeViewModel.FormularioState.Visible) {
        DialogFormularioCanje(
            form                        = formulario,
            textoMesUno                 = textoMesUno,
            textoMesDos                 = textoMesDos,
            textoMesTres                = textoMesTres,
            sugerenciasEmpresas         = sugerenciasEmpresas,
            expandedEmpresa             = expandedEmpresa,
            onEmpresaChange             = onEmpresaChange,
            onEmpresaSeleccionada       = onEmpresaSeleccionada,
            onExpandedEmpresaChange     = onExpandedEmpresaChange,
            sugerenciasLaboratorios     = sugerenciasLaboratorios,
            expandedLaboratorio         = expandedLaboratorio,
            onLaboratorioChange         = onLaboratorioChange,
            onLaboratorioSeleccionado   = onLaboratorioSeleccionado,
            onExpandedLaboratorioChange = onExpandedLaboratorioChange,
            onVencimientoChange         = onVencimientoChange,
            onMesUnoChange              = onMesUnoChange,
            onMesDosChange              = onMesDosChange,
            onMesTresChange             = onMesTresChange,
            onGuardar                   = onGuardar,
            onCancelar                  = onCerrarFormulario
        )
    }

    // Diálogo de confirmación de eliminación
    politicaAEliminar?.let { politica ->
        DialogConfirmarEliminar(
            laboratorio = politica.laboratorioId.toString(),
            onConfirmar = onConfirmarEliminar,
            onCancelar  = onCancelarEliminar
        )
    }

    Scaffold(
        bottomBar = {
            PharmaBottomAppBar(
                canjeSelected       = canjeSelected,
                navigateToScan      = navigateToScan,
                navigateToHistorial = navigateToHistorial,
                navigateToCanje     = navigateToCanje,
                navigateToConfig    = navigateToConfig
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorFondo)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text       = stringResource(R.string.title_politicas),
                    color      = ColorTexto,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text       = stringResource(R.string.descripcion_canje),
                    textAlign = TextAlign.Justify,
                    color      = ColorDim,
                    fontSize   = 18.sp,
                    fontFamily = FontFamily.Monospace
                )

                HorizontalDivider(color = ColorBorde)

                when (val state = uiState) {
                    is CanjeViewModel.UiState.Cargando -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = ColorVigente)
                        }
                    }
                    is CanjeViewModel.UiState.Vacio -> {
                        EstadoVacioCanje(onPrepoblar = onPrepoblar)
                    }
                    is CanjeViewModel.UiState.ConDatos -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(state.politicas, key = { it.politica.id }) { item ->
                                TarjetaPoliticaCanje(
                                    politica   = item.politica,
                                    nombreLaboratorio = item.laboratorio,
                                    nombreEmpresa = item.empresa,
                                    onEditar   = { onEditarPolitica(item.politica) },
                                    onEliminar = { onEliminarPolitica(item.politica) }
                                )
                            }
                        }
                    }
                    is CanjeViewModel.UiState.Error -> {
                        TarjetaErrorCanje(state.mensaje)
                    }
                }
            }

            FloatingActionButton(
                onClick        = onNuevaPolitica,
                modifier       = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                containerColor = ColorVigente,
                contentColor   = Color.White
            ) {
                Icon(
                    imageVector        = Icons.Default.Add,
                    contentDescription = stringResource(R.string.nueva_politica)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CanjeScreenContentNormalPreview() {
    PharmaDateCheckTheme {
        CanjeScreenContent(
            canjeSelected = true,
            uiState = CanjeViewModel.UiState.Vacio,
            formulario = CanjeViewModel.FormularioState.Oculto,
            politicaAEliminar = null,
            sugerenciasEmpresas = emptyList(),
            expandedEmpresa = false,
            sugerenciasLaboratorios = emptyList(),
            expandedLaboratorio = false,
            textoMesUno = "",
            textoMesDos = "",
            textoMesTres = "",
            onNuevaPolitica = {},
            onEditarPolitica = {},
            onEliminarPolitica = {},
            onConfirmarEliminar = {},
            onCancelarEliminar = {},
            onPrepoblar = {},
            onEmpresaChange = {},
            onEmpresaSeleccionada = {},
            onExpandedEmpresaChange = {},
            onLaboratorioChange = {},
            onLaboratorioSeleccionado = {},
            onExpandedLaboratorioChange = {},
            onVencimientoChange = {},
            onMesUnoChange = {},
            onMesDosChange = {},
            onMesTresChange = {},
            onGuardar = {},
            onCerrarFormulario = {},
            navigateToScan = {},
            navigateToHistorial = {},
            navigateToCanje = {},
            navigateToConfig = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CanjeScreenContentConDatosPreview() {
    PharmaDateCheckTheme {
        CanjeScreenContent(
            canjeSelected = true,
            uiState = CanjeViewModel.UiState.ConDatos(
                politicas = listOf(
                    CanjeViewModel.PoliticaConNombre(
                        politica = PoliticaCanje(
                            id           = 1,
                            empresaId    = 1,
                            laboratorioId = 3,
                            vencimiento  = true,
                            mesUno       = LocalDate.of(2026, 4, 1),
                            mesDos       = LocalDate.of(2026, 5, 1),
                            mesTres      = LocalDate.of(2026, 6, 1)
                        ),
                        empresa = "ANDROMACO S.A.",
                        laboratorio = "ANDROMACO"
                    ),
                    CanjeViewModel.PoliticaConNombre(
                        politica = PoliticaCanje(
                            id           = 2,
                            empresaId    = 2,
                            laboratorioId = 4,
                            vencimiento  = false,
                            mesUno = null,
                            mesDos = null,
                            mesTres = null
                        ),
                        empresa = "SAVAL S.A.",
                        laboratorio = "SAVAL"
                    )
                )
            ),
            formulario = CanjeViewModel.FormularioState.Oculto,
            politicaAEliminar = null,
            sugerenciasEmpresas = emptyList(),
            expandedEmpresa = false,
            sugerenciasLaboratorios = emptyList(),
            expandedLaboratorio = false,
            textoMesUno = "",
            textoMesDos = "",
            textoMesTres = "",
            onNuevaPolitica = {},
            onEditarPolitica = {},
            onEliminarPolitica = {},
            onConfirmarEliminar = {},
            onCancelarEliminar = {},
            onPrepoblar = {},
            onEmpresaChange = {},
            onEmpresaSeleccionada = {},
            onExpandedEmpresaChange = {},
            onLaboratorioChange = {},
            onLaboratorioSeleccionado = {},
            onExpandedLaboratorioChange = {},
            onVencimientoChange = {},
            onMesUnoChange = {},
            onMesDosChange = {},
            onMesTresChange = {},
            onGuardar = {},
            onCerrarFormulario = {},
            navigateToScan = {},
            navigateToHistorial = {},
            navigateToCanje = {},
            navigateToConfig = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CanjeScreenContentCargandoPreview() {
    PharmaDateCheckTheme {
        CanjeScreenContent(
            canjeSelected = true,
            uiState = CanjeViewModel.UiState.Cargando,
            formulario = CanjeViewModel.FormularioState.Oculto,
            politicaAEliminar = null,
            sugerenciasEmpresas = emptyList(),
            expandedEmpresa = false,
            sugerenciasLaboratorios = emptyList(),
            expandedLaboratorio = false,
            textoMesUno = "",
            textoMesDos = "",
            textoMesTres = "",
            onNuevaPolitica = {},
            onEditarPolitica = {},
            onEliminarPolitica = {},
            onConfirmarEliminar = {},
            onCancelarEliminar = {},
            onPrepoblar = {},
            onEmpresaChange = {},
            onEmpresaSeleccionada = {},
            onExpandedEmpresaChange = {},
            onLaboratorioChange = {},
            onLaboratorioSeleccionado = {},
            onExpandedLaboratorioChange = {},
            onVencimientoChange = {},
            onMesUnoChange = {},
            onMesDosChange = {},
            onMesTresChange = {},
            onGuardar = {},
            onCerrarFormulario = {},
            navigateToScan = {},
            navigateToHistorial = {},
            navigateToCanje = {},
            navigateToConfig = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CanjeScreenContentErrorPreview() {
    PharmaDateCheckTheme {
        CanjeScreenContent(
            canjeSelected = true,
            uiState = CanjeViewModel.UiState.Error(mensaje = "No se han encontrado políticas"),
            formulario = CanjeViewModel.FormularioState.Oculto,
            politicaAEliminar = null,
            sugerenciasEmpresas = emptyList(),
            expandedEmpresa = false,
            sugerenciasLaboratorios = emptyList(),
            expandedLaboratorio = false,
            textoMesUno = "",
            textoMesDos = "",
            textoMesTres = "",
            onNuevaPolitica = {},
            onEditarPolitica = {},
            onEliminarPolitica = {},
            onConfirmarEliminar = {},
            onCancelarEliminar = {},
            onPrepoblar = {},
            onEmpresaChange = {},
            onEmpresaSeleccionada = {},
            onExpandedEmpresaChange = {},
            onLaboratorioChange = {},
            onLaboratorioSeleccionado = {},
            onExpandedLaboratorioChange = {},
            onVencimientoChange = {},
            onMesUnoChange = {},
            onMesDosChange = {},
            onMesTresChange = {},
            onGuardar = {},
            onCerrarFormulario = {},
            navigateToScan = {},
            navigateToHistorial = {},
            navigateToCanje = {},
            navigateToConfig = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CanjeScreenContentFormularioPreview() {
    PharmaDateCheckTheme {
        CanjeScreenContent(
            canjeSelected = true,
            uiState = CanjeViewModel.UiState.Vacio,
            formulario = CanjeViewModel.FormularioState.Visible(),
            politicaAEliminar = null,
            sugerenciasEmpresas = emptyList(),
            expandedEmpresa = false,
            sugerenciasLaboratorios = emptyList(),
            expandedLaboratorio = false,
            textoMesUno = "",
            textoMesDos = "",
            textoMesTres = "",
            onNuevaPolitica = {},
            onEditarPolitica = {},
            onEliminarPolitica = {},
            onConfirmarEliminar = {},
            onCancelarEliminar = {},
            onPrepoblar = {},
            onEmpresaChange = {},
            onEmpresaSeleccionada = {},
            onExpandedEmpresaChange = {},
            onLaboratorioChange = {},
            onLaboratorioSeleccionado = {},
            onExpandedLaboratorioChange = {},
            onVencimientoChange = {},
            onMesUnoChange = {},
            onMesDosChange = {},
            onMesTresChange = {},
            onGuardar = {},
            onCerrarFormulario = {},
            navigateToScan = {},
            navigateToHistorial = {},
            navigateToCanje = {},
            navigateToConfig = {}
        )
    }
}