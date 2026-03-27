package com.lepeman.pharmadatecheck.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.repositories.EmpresaRepository
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.emptyList

class CanjeViewModel(
    private val politicaCanjeRepository: PoliticaCanjeRepository,
    private val laboratorioRepository: LaboratorioRepository,
    private val empresaRepository: EmpresaRepository
) : ViewModel() {

    // ── Estados anidados ──────────────────────────────────────────────────────

    sealed class UiState {
        object Cargando : UiState()
        object Vacio : UiState()
        data class ConDatos(val politicas: List<PoliticaCanje>) : UiState()
        data class Error(val mensaje: String) : UiState()
    }

    sealed class FormularioState {
        object Oculto : FormularioState()
        data class Visible(
            val politica: PoliticaCanje? = null,
            val razonSocial: String = "",
            val laboratorio: String = "",
            val vencimiento: Boolean = false,
            val mesUno: String = "",
            val mesDos: String = "",
            val mesTres: String = "",
            val errorEmpresa: String? = null,
            val errorLaboratorio: String? = null,
            val errorDias: String? = null
        ) : FormularioState()
    }

        // ── StateFlows ────────────────────────────────────────────────────────────

        private val _uiState = MutableStateFlow<UiState>(UiState.Cargando)
        val uiState: StateFlow<UiState> = _uiState.asStateFlow()

        private val _formulario = MutableStateFlow<FormularioState>(FormularioState.Oculto)
        val formulario: StateFlow<FormularioState> = _formulario.asStateFlow()

        private val _politicaAEliminar = MutableStateFlow<PoliticaCanje?>(null)
        val politicaAEliminar: StateFlow<PoliticaCanje?> = _politicaAEliminar.asStateFlow()

        init {
            cargarPoliticas()
        }

        // ── Carga de políticas ────────────────────────────────────────────────────

        private fun cargarPoliticas() {
            viewModelScope.launch {
                politicaCanjeRepository.todasLasPoliticas.collect { politicas ->
                    _uiState.value = if (politicas.isEmpty()) {
                        UiState.Vacio
                    } else {
                        UiState.ConDatos(politicas)
                    }
                }
            }
        }

        // ── Formulario ────────────────────────────────────────────────────────────

        fun abrirFormularioNuevo() {
            _formulario.value = FormularioState.Visible()
        }

        fun abrirFormularioEdicion(politica: PoliticaCanje) {
            _formulario.value = FormularioState.Visible(
                politica = politica,
                laboratorio = politica.laboratorio,
                diasAnticipacion = politica.diasAnticipacion.toString(),
                porcentajeRecuperacion = politica.porcentajeRecuperacion.toString(),
                condiciones = politica.condiciones ?: ""
            )
        }

        fun cerrarFormulario() {
            _formulario.value = FormularioState.Oculto
        }

        // ── Actualización de campos ───────────────────────────────────────────────

        fun onLaboratorioChange(valor: String) {
            val actual = _formulario.value as? FormularioState.Visible ?: return
            _formulario.value = actual.copy(laboratorio = valor, errorLaboratorio = null)
        }

        fun onDiasAnticipacionChange(valor: String) {
            val actual = _formulario.value as? FormularioState.Visible ?: return
            if (valor.all { it.isDigit() }) {
                _formulario.value = actual.copy(diasAnticipacion = valor, errorDias = null)
            }
        }

        fun onPorcentajeChange(valor: String) {
            val actual = _formulario.value as? FormularioState.Visible ?: return
            if (valor.all { it.isDigit() } && (valor.isEmpty() || valor.toInt() <= 100)) {
                _formulario.value = actual.copy(porcentajeRecuperacion = valor)
            }
        }

        fun onCondicionesChange(valor: String) {
            val actual = _formulario.value as? FormularioState.Visible ?: return
            _formulario.value = actual.copy(condiciones = valor)
        }

        // ── Guardar política ──────────────────────────────────────────────────────

        fun guardarPolitica() {
            val form = _formulario.value as? FormularioState.Visible ?: return

            var hayError = false

            if (form.laboratorio.isBlank()) {
                _formulario.value = form.copy(errorLaboratorio = "El nombre del laboratorio es obligatorio.")
                hayError = true
            }

            val dias = form.diasAnticipacion.toIntOrNull()
            if (dias == null || dias <= 0) {
                _formulario.value = (_formulario.value as? FormularioState.Visible ?: form)
                    .copy(errorDias = "Ingrese un número de días válido (mayor a 0).")
                hayError = true
            }

            if (hayError) return

            val porcentaje = form.porcentajeRecuperacion.toFloatOrNull() ?: 100f

            viewModelScope.launch {
                if (form.politica == null) {
                    politicaCanjeRepository.insertar(
                        PoliticaCanje(
                            laboratorio = form.laboratorio.trim(),
                            diasAnticipacion = dias!!,
                            porcentajeRecuperacion = porcentaje,
                            condiciones = form.condiciones.trim().ifBlank { null }
                        )
                    )
                } else {
                    politicaCanjeRepository.actualizar(
                        form.politica.copy(
                            laboratorio = form.laboratorio.trim(),
                            diasAnticipacion = dias!!,
                            porcentajeRecuperacion = porcentaje,
                            condiciones = form.condiciones.trim().ifBlank { null }
                        )
                    )
                }
                cerrarFormulario()
            }
        }

        // ── Eliminación ───────────────────────────────────────────────────────────

        fun solicitarEliminar(politica: PoliticaCanje) {
            _politicaAEliminar.value = politica
        }

        fun confirmarEliminar() {
            viewModelScope.launch {
                _politicaAEliminar.value?.let { politica ->
                    politicaCanjeRepository.eliminarPorId(politica.id)
                    _politicaAEliminar.value = null
                }
            }
        }

        fun cancelarEliminar() {
            _politicaAEliminar.value = null
        }

        // ── Prepoblado inicial ────────────────────────────────────────────────────

        fun prepoblarSiVacio() {
            viewModelScope.launch {
                if (_uiState.value is UiState.Vacio) {
                    politicaCanjeRepository.prepoblarSiVacio()
                }
            }
        }
}