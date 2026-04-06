package com.lepeman.pharmadatecheck.ui.viewmodels

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

/**
 * ViewModel para la pantalla de historial de sesiones de revisión.
 *
 * Gestiona dos niveles de navegación: la lista de sesiones registradas
 * ([UiState]) y el detalle de una sesión seleccionada ([DetalleSesionState]).
 * Resuelve el nombre del auxiliar de cada sesión desde [AuxiliarRepository]
 * antes de exponerlo a la UI mediante [SesionConNombre].
 *
 * @param auxiliarRepository Repositorio para resolver nombres de auxiliares.
 * @param sesionRevisionRepository Repositorio de sesiones de revisión.
 * @param productoRevisadoRepository Repositorio de productos revisados por sesión.
 */
class HistorialViewModel(
    private val auxiliarRepository: AuxiliarRepository,
    private val sesionRevisionRepository: SesionRevisionRepository,
    private val productoRevisadoRepository: ProductoRevisadoRepository
) : ViewModel() {

    // ── Estados ───────────────────────────────────────────────────────────────

    /**
     * Estado de la lista de sesiones de revisión.
     *
     * - [Cargando]: cargando sesiones desde la base de datos.
     * - [Vacio]: no hay sesiones registradas.
     * - [ConDatos]: lista de sesiones con nombres de auxiliares resueltos.
     * - [Error]: error al cargar las sesiones.
     */
    sealed class UiState {
        object Cargando : UiState()
        object Vacio : UiState()
        data class ConDatos(val sesiones: List<SesionConNombre>) : UiState()
        data class Error(val mensaje: String) : UiState()
    }

    /**
     * Estado del detalle de una sesión seleccionada.
     *
     * - [Cerrado]: no hay sesión seleccionada, se muestra la lista.
     * - [Cargando]: cargando los productos de la sesión seleccionada.
     * - [Visible]: detalle disponible con la sesión y sus productos.
     */
    sealed class DetalleSesionState {
        object Cerrado  : DetalleSesionState()
        object Cargando : DetalleSesionState()
        data class Visible(
            val sesion: SesionRevision,
            val productos: List<ProductoRevisado>
        ) : DetalleSesionState()
    }

    /**
     * Modelo de presentación que combina una [SesionRevision] con el nombre
     * del auxiliar responsable, resuelto desde [AuxiliarRepository].
     *
     * @property sesion Sesión de revisión.
     * @property nombreAuxiliar Nombre completo del auxiliar, o
     * "Auxiliar #id" si no se encuentra en el catálogo.
     */
    data class SesionConNombre(
        val sesion: SesionRevision,
        val nombreAuxiliar: String
    )

    // ── StateFlows ────────────────────────────────────────────────────────────

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

    // ── Carga de sesiones ─────────────────────────────────────────────────────

    /**
     * Suscribe el estado de la UI al flujo de sesiones del repositorio.
     * Resuelve el nombre del auxiliar de cada sesión antes de actualizar
     * el estado, de modo que la UI nunca muestra identificadores numéricos.
     */
    private fun cargarSesiones() {
        viewModelScope.launch {
            sesionRevisionRepository.obtenerTodas().collect { sesiones ->
                val conNombres = sesiones.map { sesion ->
                    val nombre = auxiliarRepository.obtenerAuxiliarPorId(sesion.auxiliarId)
                        ?.nombreAuxiliar ?: "Auxiliar #${sesion.auxiliarId}"
                    SesionConNombre(sesion = sesion, nombreAuxiliar = nombre)
                }
                _uiState.value = if (conNombres.isEmpty()) UiState.Vacio
                else UiState.ConDatos(conNombres)
            }
        }
    }

    // ── Eliminación ───────────────────────────────────────────────────────────

    /** Registra la sesión candidata a eliminar para mostrar el diálogo de confirmación. */
    fun solicitarEliminar(sesion: SesionRevision) {
        _sesionAEliminar.value = sesion
    }

    /**
     * Ejecuta la eliminación de la sesión candidata y cierra el detalle si
     * estaba visible. La eliminación en cascada de Room elimina también todos
     * los productos revisados asociados a la sesión.
     */
    fun confirmarEliminar() {
        viewModelScope.launch {
            _sesionAEliminar.value?.let { sesion ->
                sesionRevisionRepository.eliminar(sesion)
                _sesionAEliminar.value = null
                cerrarDetalle()
            }
        }
    }

    /** Cancela la eliminación pendiente sin modificar la base de datos. */
    fun cancelarEliminar() {
        _sesionAEliminar.value = null
    }

    // ── Detalle de sesión ─────────────────────────────────────────────────────

    /**
     * Carga el detalle de la [sesion] seleccionada y suscribe el estado
     * del detalle al flujo de productos del repositorio. Mientras los
     * productos se cargan, el estado transiciona a [DetalleSesionState.Cargando].
     */
    fun seleccionarSesion(sesion: SesionRevision) {
        viewModelScope.launch {
            _detalleState.value = DetalleSesionState.Cargando
            productoRevisadoRepository.obtenerPorSesion(sesion.id).collect { productos ->
                _detalleState.value = DetalleSesionState.Visible(
                    sesion   = sesion,
                    productos = productos
                )
            }
        }
    }

    /**
     * Cierra el detalle de sesión y restablece el filtro de clasificación,
     * volviendo a la vista de lista de sesiones.
     */
    fun cerrarDetalle() {
        _detalleState.value        = DetalleSesionState.Cerrado
        _filtroClasificacion.value = null
    }
}