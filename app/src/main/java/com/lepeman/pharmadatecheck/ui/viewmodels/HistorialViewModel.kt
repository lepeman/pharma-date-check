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
            val sesiones: List<SesionConNombre>
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

    data class SesionConNombre(
        val sesion: SesionRevision,
        val nombreAuxiliar: String
    )

    private val _uiState = MutableStateFlow<UiState>(UiState.Cargando)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _detalleState = MutableStateFlow<DetalleSesionState>(DetalleSesionState.Cerrado)
    val detalleState: StateFlow<DetalleSesionState> = _detalleState.asStateFlow()

    private val _sesiones = MutableStateFlow<List<SesionConNombre>>(emptyList())
    val sesiones: StateFlow<List<SesionConNombre>> = _sesiones.asStateFlow()

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
                val conNombres = sesiones.map { sesion ->
                    val nombre = auxiliarRepository.obtenerAuxiliarPorId(sesion.auxiliarId)
                        ?.nombreAuxiliar ?: "Auxiliar #${sesion.auxiliarId}"
                    SesionConNombre(sesion = sesion, nombreAuxiliar = nombre)
                }
                _sesiones.value = conNombres
                _uiState.value = if (conNombres.isEmpty()) {
                    UiState.Vacio
                } else {
                    UiState.ConDatos(conNombres)
                }
            }
        }
    }

    fun solicitarEliminar(sesion: SesionRevision) {
        _sesionAEliminar.value = sesion
    }

    fun confirmarEliminar() {
        viewModelScope.launch {
            _sesionAEliminar.value?.let { sesion ->
                sesionRevisionRepository.eliminar(sesion)
                _sesionAEliminar.value = null
                cerrarDetalle()
            }
        }
    }

    fun cancelarEliminar() {
        _sesionAEliminar.value = null
    }

    fun seleccionarSesion(sesion: SesionRevision) {
        viewModelScope.launch {
            _detalleState.value = DetalleSesionState.Cargando
            productoRevisadoRepository.obtenerPorSesion(sesion.id).collect { productos ->
                _detalleState.value = DetalleSesionState.Visible(
                    sesion = sesion,
                    productos = productos
                )
            }
        }
    }

    fun aplicarFiltroClasificacion() {}

    fun cerrarDetalle() {
        _detalleState.value = DetalleSesionState.Cerrado
        _filtroClasificacion.value = null
    }



    private fun filtrarProductos() {}





}