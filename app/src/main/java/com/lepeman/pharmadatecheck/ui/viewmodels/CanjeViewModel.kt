package com.lepeman.pharmadatecheck.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.repositories.EmpresaRepository
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CanjeViewModel(
    private val politicaCanjeRepository: PoliticaCanjeRepository,
    private val laboratorioRepository: LaboratorioRepository,
    private val empresaRepository: EmpresaRepository
) : ViewModel() {

    sealed class UiState {
        object Cargando : UiState()
        object Vacio : UiState()
        data class ConDatos(val politicas: List<PoliticaCanje>) : UiState()
        data class Error(val mensaje: String) : UiState()
    }

    sealed class FormularioState {
        object Oculto: FormularioState()
        data class Visible(
            val politica: PoliticaCanje? = null,
            val nombreLaboratorio: String = "",
            val nombreEmpresa: String = "",
            val vencimiento: String = "",
            val fechaUno: String = "",
            val fechaDos: String = "",
            val fechaTres: String = "",
            val errorLaboratorio: String? = null,
            val errorEmpresa: String? = null
        ): FormularioState()
    }

    // Estado principal - lista de políticas
    private val _uiState = MutableStateFlow<UiState>(UiState.Cargando)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Estado del formulario
    private val _formulario = MutableStateFlow<FormularioState>(FormularioState.Oculto)
    val formulario: StateFlow<FormularioState> = _formulario.asStateFlow()

    private val _politicaAEliminar = MutableStateFlow<PoliticaCanje?>(null)
    val politicaAEliminar: StateFlow<PoliticaCanje?> = _politicaAEliminar.asStateFlow()

    init {
        cargarPoliticas()
    }

    private fun cargarPoliticas() {
        viewModelScope.launch {
            politicaCanjeRepository.obtenerTodas().collect { politicas ->
                _uiState.value = if (politicas.isEmpty()) {
                    UiState.Vacio
                } else {
                    UiState.ConDatos(politicas)
                }
            }
        }
    }

    fun abrirFormularioNuevo() {
        _formulario.value = FormularioState.Visible()
    }

    fun abrirFormularioEdicion(politica: PoliticaCanje) {
        _formulario.value = FormularioState.Visible(
            politica = politica,
            nombreLaboratorio = laboratorioRepository.obtenerNombrePorId(politica.laboratorioId),
            nombreEmpresa = empresaRepository.obtenerNombreEmpresa(politica.empresaId),
            vencimiento = if (politica.vencimiento) "SI" else "NO",
            fechaUno = politica.mesUno.toFormatoFormulario(),
            fechaDos = politica.mesDos.toFormatoFormulario(),
            fechaTres = politica.mesTres.toFormatoFormulario()
        )
    }

    fun LocalDate?.toFormatoFormulario(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return this?.format(formatter) ?: ""
    }

    fun onLaboratorioChange(valor: String) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(
            nombreLaboratorio = valor,
            errorLaboratorio = null
        )
    }

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

    fun prepoblarSiVacio() {
        viewModelScope.launch {
            if (_uiState.value is UiState.Vacio) {
                politicaCanjeRepository.prepoblarSiVacio()
            }
        }
    }

}