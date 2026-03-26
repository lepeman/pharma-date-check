package com.lepeman.pharmadatecheck.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import com.lepeman.pharmadatecheck.data.repositories.AuxiliarRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRevisadoRepository
import com.lepeman.pharmadatecheck.data.repositories.SesionRevisionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistorialViewModel(
    private val auxiliarRepository: AuxiliarRepository,
    private val sesionRevisionRepository: SesionRevisionRepository,
    private val productoRevisadoRepository: ProductoRevisadoRepository
) : ViewModel() {

    sealed class UiState {
        object Cargando : UiState()
        object Vacio : UiState()
        data class ConDatos(
            val sesiones: List<SesionRevision>
        ) : UiState()
        data class Error(val mensaje: String) : UiState()
    }

    sealed class DetalleSesionState {
        object Cerrado : DetalleSesionState()
        object Cargando : DetalleSesionState()
        data class Visible(
            val sesion: SesionRevision,
            val productos: List<ProductoRevisado>
        ) : DetalleSesionState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Cargando)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _detalleState = MutableStateFlow<DetalleSesionState>(DetalleSesionState.Cerrado)
    val detalleState: StateFlow<DetalleSesionState> = _detalleState.asStateFlow()

    private val _filtroClasificacion = MutableStateFlow<String?>(null)
    val filtroClasificacion: StateFlow<String?> = _filtroClasificacion.asStateFlow()

    private val _sesionAEliminar = MutableStateFlow<SesionRevision?>(null)
    val sesionAEliminar: StateFlow<SesionRevision?> = _sesionAEliminar.asStateFlow()

    init {
        cargarSesiones()
    }

    private fun cargarSesiones() {
        viewModelScope.launch {
            sesionRevisionRepository.obtenerTodas().collect { sesiones ->
                _uiState.value = if (sesiones.isEmpty()) {
                    UiState.Vacio
                } else {
                    UiState.ConDatos(sesiones)
                }
            }
        }
    }

    fun aplicarFiltroClasificacion() {}

    fun cerrarDetalle() {

    }

    fun seleccionarSesion() {}

    private fun filtrarProductos() {}

    fun confirmarEliminar() {
    }

    fun cancelarEliminar() {}

}