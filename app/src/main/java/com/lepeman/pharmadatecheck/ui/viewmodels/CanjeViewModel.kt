package com.lepeman.pharmadatecheck.ui.viewmodels

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

/**
 * ViewModel para la pantalla de gestión de políticas de canje.
 *
 * Gestiona el ciclo de vida completo de una política de canje: listado,
 * creación, edición y eliminación. Implementa autocompletado con debounce
 * de 300ms para los campos de empresa y laboratorio del formulario,
 * evitando consultas excesivas a la base de datos durante la escritura.
 *
 * @param politicaCanjeRepository Repositorio de políticas de canje.
 * @param laboratorioRepository Repositorio del catálogo de laboratorios.
 * @param empresaRepository Repositorio del catálogo de empresas.
 */
class CanjeViewModel(
    private val politicaCanjeRepository: PoliticaCanjeRepository,
    private val laboratorioRepository: LaboratorioRepository,
    private val empresaRepository: EmpresaRepository
) : ViewModel() {

    // ── Estados ───────────────────────────────────────────────────────────────

    data class PoliticaConNombre(
        val politica: PoliticaCanje,
        val empresa: String,
        val laboratorio: String
    )

    /**
     * Estado de la lista de políticas de canje.
     */
    sealed class UiState {
        object Cargando : UiState()
        object Vacio : UiState()
        data class ConDatos(val politicas: List<PoliticaConNombre>) : UiState()
        data class Error(val mensaje: String) : UiState()
    }

    /**
     * Estado del formulario de creación y edición de políticas.
     *
     * Incluye utilidades de conversión entre [LocalDate] y el formato de
     * texto MM/yyyy usado en la interfaz de usuario.
     */
    sealed class FormularioState {

        companion object {
            /**
             * Formatea una [LocalDate] al formato MM/yyyy para mostrar en el
             * campo de texto, o retorna cadena vacía si la fecha es null.
             */
            fun format(fecha: LocalDate?): String =
                fecha?.format(DateTimeFormatter.ofPattern("MM/yyyy")) ?: ""

            /**
             * Convierte un texto en formato MM/yyyy a [LocalDate] con día 1,
             * o retorna null si el texto no es una fecha válida.
             */
            fun toDate(texto: String): LocalDate? {
                return try {
                    YearMonth.parse(texto, DateTimeFormatter.ofPattern("MM/yyyy")).atDay(1)
                } catch (e: Exception) {
                    null
                }
            }
        }

        /** El formulario está oculto. */
        object Oculto : FormularioState()

        /** El formulario está visible con los valores actuales del formulario. */
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

    private val _sugerenciasEmpresas = MutableStateFlow<List<Empresa>>(emptyList())
    val sugerenciasEmpresas = _sugerenciasEmpresas.asStateFlow()

    private val _expandedEmpresa = MutableStateFlow(false)
    val expandedEmpresa: StateFlow<Boolean> = _expandedEmpresa.asStateFlow()

    private val _sugerenciasLaboratorios = MutableStateFlow<List<Laboratorio>>(emptyList())
    val sugerenciasLaboratorios = _sugerenciasLaboratorios.asStateFlow()

    private val _expandedLaboratorio = MutableStateFlow(false)
    val expandedLaboratorio: StateFlow<Boolean> = _expandedLaboratorio.asStateFlow()

    private var searchJobEmpresa: Job?     = null
    private var searchJobLaboratorio: Job? = null

    private val _uiState = MutableStateFlow<UiState>(UiState.Cargando)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _formulario = MutableStateFlow<FormularioState>(FormularioState.Oculto)
    val formulario: StateFlow<FormularioState> = _formulario.asStateFlow()

    private val _politicaAEliminar = MutableStateFlow<PoliticaCanje?>(null)
    val politicaAEliminar: StateFlow<PoliticaCanje?> = _politicaAEliminar.asStateFlow()

    private val _textoMesUno  = MutableStateFlow("")
    val textoMesUno: StateFlow<String> = _textoMesUno.asStateFlow()

    private val _textoMesDos  = MutableStateFlow("")
    val textoMesDos: StateFlow<String> = _textoMesDos.asStateFlow()

    private val _textoMesTres = MutableStateFlow("")
    val textoMesTres: StateFlow<String> = _textoMesTres.asStateFlow()

    init {
        cargarPoliticas()
    }

    // ── Carga de políticas ────────────────────────────────────────────────────

    /**
     * Suscribe el estado de la UI al flujo de políticas del repositorio.
     * Se actualiza automáticamente ante cualquier cambio en la tabla.
     */
    private fun cargarPoliticas() {
        viewModelScope.launch {
            politicaCanjeRepository.obtenerTodas().collect { politicas ->
                val conNombres = politicas.map { politica ->
                    PoliticaConNombre(
                        politica = politica,
                        empresa = laboratorioRepository.obtenerNombrePorId(politica.empresaId) ?: "Empresa: ${politica.empresaId}",
                        laboratorio = laboratorioRepository.obtenerNombrePorId(politica.laboratorioId) ?: "Laboratorio: ${politica.laboratorioId}"
                    )
                }
                _uiState.value = if (conNombres.isEmpty()) UiState.Vacio
                else UiState.ConDatos(conNombres)
            }
        }
    }

    // ── Formulario ────────────────────────────────────────────────────────────

    /** Abre el formulario en modo creación con todos los campos vacíos. */
    fun abrirFormularioNuevo() {
        _textoMesUno.value  = ""
        _textoMesDos.value  = ""
        _textoMesTres.value = ""
        _formulario.value   = FormularioState.Visible()
    }

    /**
     * Abre el formulario en modo edición con los valores de [politica]
     * precargados. Resuelve los nombres de empresa y laboratorio desde
     * sus repositorios antes de poblar el formulario.
     */
    fun abrirFormularioEdicion(politica: PoliticaCanje) {
        viewModelScope.launch {
            _textoMesUno.value  = FormularioState.format(politica.mesUno)
            _textoMesDos.value  = FormularioState.format(politica.mesDos)
            _textoMesTres.value = FormularioState.format(politica.mesTres)

            _formulario.value = FormularioState.Visible(
                politica         = politica,
                razonSocial      = empresaRepository.obtenerNombreEmpresa(politica.empresaId),
                laboratorio      = laboratorioRepository.obtenerNombrePorId(politica.laboratorioId) ?: "",
                vencimiento      = politica.vencimiento,
                mesUno           = politica.mesUno,
                mesDos           = politica.mesDos,
                mesTres          = politica.mesTres,
                errorEmpresa     = null,
                errorLaboratorio = null,
                errorDias        = null
            )
        }
    }

    /** Cierra el formulario sin guardar cambios. */
    fun cerrarFormulario() {
        _formulario.value = FormularioState.Oculto
    }

    // ── Autocompletado de empresa ─────────────────────────────────────────────

    /**
     * Actualiza el campo de razón social y lanza una búsqueda con debounce
     * de 300ms. Cancela la búsqueda anterior antes de lanzar la nueva.
     */
    fun onEmpresaChange(consulta: String) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(razonSocial = consulta, errorEmpresa = null)

        searchJobEmpresa?.cancel()

        if (consulta.length < 2) {
            _sugerenciasEmpresas.value = emptyList()
            _expandedEmpresa.value     = false
            return
        }

        searchJobEmpresa = viewModelScope.launch {
            delay(300)
            empresaRepository.buscarItems(consulta).collect { resultados ->
                _sugerenciasEmpresas.value = resultados
                _expandedEmpresa.value     = resultados.isNotEmpty()
            }
        }
    }

    /** Confirma la selección de una empresa del menú de autocompletado. */
    fun onEmpresaSeleccionada(empresa: Empresa) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value          = actual.copy(razonSocial = empresa.razonSocial, errorEmpresa = null)
        _sugerenciasEmpresas.value = emptyList()
        _expandedEmpresa.value     = false
    }

    /** Actualiza el estado de expansión del menú de empresas. */
    fun onExpandedEmpresaChange(expanded: Boolean) {
        _expandedEmpresa.value = expanded
    }

    // ── Autocompletado de laboratorio ─────────────────────────────────────────

    /**
     * Actualiza el campo de laboratorio y lanza una búsqueda con debounce
     * de 300ms. Cancela la búsqueda anterior antes de lanzar la nueva.
     */
    fun onLaboratorioChange(valor: String) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(laboratorio = valor, errorLaboratorio = null)

        searchJobLaboratorio?.cancel()

        if (valor.length < 2) {
            _sugerenciasLaboratorios.value = emptyList()
            _expandedLaboratorio.value     = false
            return
        }

        searchJobLaboratorio = viewModelScope.launch {
            delay(300)
            laboratorioRepository.buscarItems(valor).collect { resultados ->
                _sugerenciasLaboratorios.value = resultados
                _expandedLaboratorio.value     = resultados.isNotEmpty()
            }
        }
    }

    /** Confirma la selección de un laboratorio del menú de autocompletado. */
    fun onLaboratorioSeleccionado(laboratorio: Laboratorio) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value              = actual.copy(laboratorio = laboratorio.nombre, errorLaboratorio = null)
        _sugerenciasLaboratorios.value = emptyList()
        _expandedLaboratorio.value     = false
    }

    /** Actualiza el estado de expansión del menú de laboratorios. */
    fun onExpandedLaboratorioChange(expanded: Boolean) {
        _expandedLaboratorio.value = expanded
    }

    /** Actualiza el estado del interruptor de canje por vencimiento. */
    fun onVencimientoChange(valor: Boolean) {
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(vencimiento = valor)
    }

    /** Actualiza el texto crudo y la fecha parseada del primer mes de canje. */
    fun onMesUnoChange(valor: String) {
        _textoMesUno.value = valor
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(mesUno = FormularioState.toDate(valor))
    }

    /** Actualiza el texto crudo y la fecha parseada del segundo mes de canje. */
    fun onMesDosChange(valor: String) {
        _textoMesDos.value = valor
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(mesDos = FormularioState.toDate(valor))
    }

    /** Actualiza el texto crudo y la fecha parseada del tercer mes de canje. */
    fun onMesTresChange(valor: String) {
        _textoMesTres.value = valor
        val actual = _formulario.value as? FormularioState.Visible ?: return
        _formulario.value = actual.copy(mesTres = FormularioState.toDate(valor))
    }

    // ── Guardar política ──────────────────────────────────────────────────────

    /**
     * Valida el formulario y persiste la política de canje.
     *
     * Si el laboratorio está en blanco, establece el error correspondiente
     * y aborta sin guardar. En caso contrario, inserta o actualiza la
     * política según si [FormularioState.Visible.politica] es null o no,
     * y cierra el formulario al finalizar.
     */
    fun guardarPolitica() {
        val form = _formulario.value as? FormularioState.Visible ?: return

        if (form.laboratorio.isBlank()) {
            _formulario.value = form.copy(errorLaboratorio = "El nombre del laboratorio es obligatorio.")
            return
        }

        viewModelScope.launch {
            if (form.politica == null) {
                politicaCanjeRepository.insertar(
                    PoliticaCanje(
                        empresaId     = empresaRepository.obtenerIdPorNombre(form.razonSocial.trim()) ?: 0,
                        laboratorioId = laboratorioRepository.obtenerIdPorNombre(form.laboratorio.trim()),
                        vencimiento   = form.vencimiento,
                        mesUno        = form.mesUno,
                        mesDos        = form.mesDos,
                        mesTres       = form.mesTres
                    )
                )
            } else {
                politicaCanjeRepository.actualizar(
                    form.politica.copy(
                        empresaId     = empresaRepository.obtenerIdPorNombre(form.razonSocial.trim()) ?: 0,
                        laboratorioId = laboratorioRepository.obtenerIdPorNombre(form.laboratorio.trim()),
                        vencimiento   = form.vencimiento,
                        mesUno        = form.mesUno,
                        mesDos        = form.mesDos,
                        mesTres       = form.mesTres
                    )
                )
            }
            cerrarFormulario()
        }
    }

    // ── Eliminación ───────────────────────────────────────────────────────────

    /** Registra la política candidata a eliminar para mostrar el diálogo de confirmación. */
    fun solicitarEliminar(politica: PoliticaCanje) {
        _politicaAEliminar.value = politica
    }

    /** Ejecuta la eliminación de la política candidata y limpia el estado. */
    fun confirmarEliminar() {
        viewModelScope.launch {
            _politicaAEliminar.value?.let { politica ->
                politicaCanjeRepository.eliminarPorId(politica.id)
                _politicaAEliminar.value = null
            }
        }
    }

    /** Cancela la eliminación pendiente sin modificar la base de datos. */
    fun cancelarEliminar() {
        _politicaAEliminar.value = null
    }

    // ── Prepoblado inicial ────────────────────────────────────────────────────

    /**
     * Inserta un conjunto de políticas de ejemplo si la tabla está vacía.
     * Solo se ejecuta cuando el estado actual es [UiState.Vacio].
     */
    fun prepoblarSiVacio() {
        viewModelScope.launch {
            if (_uiState.value is UiState.Vacio) {
                politicaCanjeRepository.prepoblarSiVacio()
            }
        }
    }
}