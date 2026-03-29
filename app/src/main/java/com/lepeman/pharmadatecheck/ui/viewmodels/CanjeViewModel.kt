package com.lepeman.pharmadatecheck.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.repositories.EmpresaRepository
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

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

        companion object {
            fun format(fecha: LocalDate?): String {
                return fecha?.format(DateTimeFormatter.ofPattern("MM/yyyy")) ?: ""
            }

            fun toDate(texto: String): LocalDate? {
                return try {
                    val yearMonth = YearMonth.parse(texto, DateTimeFormatter.ofPattern("MM/yyyy"))
                    yearMonth.atDay(1)
                } catch(e: Exception) {
                    e.printStackTrace()
                    null // Si el usuario escribe algo mal, retornamos null de manera segura
                }
            }
        }

        object Oculto : FormularioState()
        data class Visible(
            val politica: PoliticaCanje? = null,
            val razonSocial: String = "",
            val laboratorio: String = "",
            val vencimiento: Boolean = false,
            val mesUno: LocalDate? = null,
            val mesDos: LocalDate? = null,
            val mesTres: LocalDate? = null,
            val errorEmpresa: String? = null,
            val errorLaboratorio: String? = null,
            val errorDias: String? = null
        ) : FormularioState()
    }

    // ── StateFlows ────────────────────────────────────────────────────────────

    var consultaBusqueda by mutableStateOf("")
        private set

    private val _sugerenciasEmpresas = MutableStateFlow<List<Empresa>>(emptyList())
    val sugerenciasEmpresas = _sugerenciasEmpresas.asStateFlow()

    private val _expandedEmpresa = MutableStateFlow(false)
    val expandedEmpresa: StateFlow<Boolean> = _expandedEmpresa.asStateFlow()

    private val _sugerenciasLaboratorios = MutableStateFlow<List<Laboratorio>>(emptyList())
    val sugerenciasLaboratorios = _sugerenciasLaboratorios.asStateFlow()

    private val _expandedLaboratorio = MutableStateFlow(false)
    val expandedLaboratorio: StateFlow<Boolean> = _expandedLaboratorio.asStateFlow()

    private var searchJobEmpresa: Job? = null
    private var searchJobLaboratorio: Job? = null

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
            politicaCanjeRepository.obtenerTodas().collect { politicas ->
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
            razonSocial = empresaRepository.obtenerNombreEmpresa(politica.laboratorioId),
            laboratorio = laboratorioRepository.obtenerNombrePorId(politica.laboratorioId) ?: "",
            vencimiento = politica.vencimiento,
            mesUno = politica.mesUno,
            mesDos = politica.mesDos,
            mesTres = politica.mesTres,
            errorEmpresa = null,
            errorLaboratorio = null,
            errorDias = null
        )
    }

    fun cerrarFormulario() {
        _formulario.value = FormularioState.Oculto
    }

    // ── Búsqueda de Empresa ───────────────────────────────────────────────

    fun onEmpresaChange(consulta: String) {

        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(razonSocial = consulta, errorEmpresa = null)

        searchJobEmpresa?.cancel()

        if (consulta.length < 2) {
            _sugerenciasEmpresas.value = emptyList()
            _expandedEmpresa.value = false
            return
        }

        searchJobEmpresa = viewModelScope.launch {
            delay(300)
            empresaRepository.buscarItems(consulta).collect { resultados ->
                _sugerenciasEmpresas.value = resultados
                _expandedEmpresa.value = resultados.isNotEmpty()
            }
        }
    }

    fun onEmpresaSeleccionada(empresa: Empresa) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(razonSocial = empresa.razonSocial, errorEmpresa = null)
        _sugerenciasEmpresas.value = emptyList()
        _expandedLaboratorio.value = false
    }

    fun onExpandedEmpresaChange(expanded: Boolean) {
        _expandedEmpresa.value = expanded
    }

    // ── Búsqueda de Laboratorio ───────────────────────────────────────────────

    fun onLaboratorioChange(valor: String) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(laboratorio = valor, errorLaboratorio = null)

        searchJobLaboratorio?.cancel()

        if (valor.length < 2) {
            _sugerenciasLaboratorios.value = emptyList()
            _expandedLaboratorio.value = false
            return
        }

        searchJobLaboratorio = viewModelScope.launch {
            delay(300)
            laboratorioRepository.buscarItems(valor).collect { resultados ->
                _sugerenciasLaboratorios.value = resultados
                _expandedLaboratorio.value = resultados.isNotEmpty()
            }
        }
    }

    fun onLaboratorioSeleccionado(laboratorio: Laboratorio) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(laboratorio = laboratorio.nombre, errorLaboratorio = null)
        _sugerenciasLaboratorios.value = emptyList()
        _expandedLaboratorio.value = false
    }

    fun onExpandedLaboratorioChange(expanded: Boolean) {
        _expandedLaboratorio.value = expanded
    }

    fun onVencimientoChange(valor: Boolean) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(vencimiento = valor)
    }

    fun onMesUnoChange(valor: String) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(mesUno = FormularioState.toDate(texto = valor))
    }

    fun onMesDosChange(valor: String) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(mesDos = FormularioState.toDate(texto = valor))
    }

    fun onMesTresChange(valor: String) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(mesTres = FormularioState.toDate(texto = valor))
    }

    // ── Guardar política ──────────────────────────────────────────────────────

    fun guardarPolitica() {
        val form = _formulario.value as? FormularioState.Visible ?: return

        var hayError = false

        if (form.laboratorio.isBlank()) {
            _formulario.value = form.copy(errorLaboratorio = "El nombre del laboratorio es obligatorio.")
            hayError = true
        }

//            val dias = form.diasAnticipacion.toIntOrNull()
//            if (dias == null || dias <= 0) {
//                _formulario.value = (_formulario.value as? FormularioState.Visible ?: form)
//                    .copy(errorDias = "Ingrese un número de días válido (mayor a 0).")
//                hayError = true
//            }
//
//            if (hayError) return
//
//            val porcentaje = form.porcentajeRecuperacion.toFloatOrNull() ?: 100f

        viewModelScope.launch {
            if (form.politica == null) {
                politicaCanjeRepository.insertar(
                    PoliticaCanje(
                        empresaId = empresaRepository.obtenerIdPorNombre(form.razonSocial.trim()),
                        laboratorioId = laboratorioRepository.obtenerIdPorNombre(form.laboratorio.trim()),
                        vencimiento = form.vencimiento,
                        mesUno = form.mesUno,
                        mesDos = form.mesDos,
                        mesTres = form.mesTres
                    )
                )
            } else {
                politicaCanjeRepository.actualizar(
                    form.politica.copy(
                        empresaId = empresaRepository.obtenerIdPorNombre(form.razonSocial.trim()),
                        laboratorioId = laboratorioRepository.obtenerIdPorNombre(form.laboratorio.trim()),
                        vencimiento = form.vencimiento,
                        mesUno = form.mesUno,
                        mesDos = form.mesDos,
                        mesTres = form.mesTres
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