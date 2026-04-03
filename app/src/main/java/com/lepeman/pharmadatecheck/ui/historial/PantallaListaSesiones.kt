package com.lepeman.pharmadatecheck.ui.historial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import com.lepeman.pharmadatecheck.ui.theme.ColorBorde
import com.lepeman.pharmadatecheck.ui.theme.ColorDim
import com.lepeman.pharmadatecheck.ui.theme.ColorTexto
import com.lepeman.pharmadatecheck.ui.viewmodels.HistorialViewModel

@Composable
fun PantallaListaSesiones(
    uiState: HistorialViewModel.UiState,
    onSeleccionarSesion: (SesionRevision) -> Unit,
    onEliminarSesion: (SesionRevision) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.historial_revisiones),
            color = ColorTexto,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = "Toque una sesión para ver el detalle",
            color = ColorDim,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )

        HorizontalDivider(color = ColorBorde)

        when (uiState) {
            is HistorialViewModel.UiState.Cargando -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ColorTexto)
                }
            }
            is HistorialViewModel.UiState.Vacio -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No hay sesiones registradas.\nComplete una revisión para verla aquí.",
                        color = ColorDim,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                }
            }
            is HistorialViewModel.UiState.ConDatos -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items = uiState.sesiones, key = { it.sesion.id }) { item ->
                        TarjetaSesion(
                            sesion = item.sesion,
                            nombreAuxiliar = item.nombreAuxiliar,
                            onClick = { onSeleccionarSesion(item.sesion) },
                            onEliminar = { onEliminarSesion(item.sesion) }
                        )
                    }
                }
            }
            is HistorialViewModel.UiState.Error -> {
                TarjetaErrorHistorial(uiState.mensaje)
            }
        }
    }
}