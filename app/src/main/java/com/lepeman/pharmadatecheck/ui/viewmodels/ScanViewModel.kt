package com.lepeman.pharmadatecheck.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import com.lepeman.pharmadatecheck.data.repositories.AuxiliarRepository
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRevisadoRepository
import com.lepeman.pharmadatecheck.data.repositories.SesionRevisionRepository
import com.lepeman.pharmadatecheck.domain.Clasificacion
import com.lepeman.pharmadatecheck.domain.ClasificadorProducto
import com.lepeman.pharmadatecheck.domain.ResultadoClasificacion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * ViewModel para la pantalla de escaneo de productos.
 *
 * Gestiona el ciclo completo de una sesión de revisión:
 * 1. Autenticación del auxiliar por RUT ([iniciarSesion]).
 * 2. Captura del código EAN-13 vía HID ([onCaracterHid]) o manual ([procesarEAN13Manual]).
 * 3. Solicitud de fecha de vencimiento al operador mediante [_ean13Pendiente].
 * 4. Clasificación del producto ([confirmarFechaYClasificar]) y actualización de contadores.
 * 5. Cierre de sesión con persistencia de totales ([cerrarSesion]).
 *
 * @param auxiliarRepository Repositorio para autenticar auxiliares por RUT.
 * @param laboratorioRepository Repositorio para resolver nombres de laboratorios.
 * @param productoRepository Repositorio del catálogo de productos.
 * @param politicaCanjeRepository Repositorio de políticas de canje por laboratorio.
 * @param sesionRevisionRepository Repositorio de sesiones de revisión.
 * @param productoRevisadoRepository Repositorio para registrar productos revisados.
 */
class ScanViewModel(
    private val auxiliarRepository: AuxiliarRepository,
    private val laboratorioRepository: LaboratorioRepository,
    private val productoRepository: ProductoRepository,
    private val politicaCanjeRepository: PoliticaCanjeRepository,
    private val sesionRevisionRepository: SesionRevisionRepository,
    private val productoRevisadoRepository: ProductoRevisadoRepository
) : ViewModel() {

    // ── Estados ───────────────────────────────────────────────────────────────

    /**
     * Estado del proceso de clasificación de un producto.
     *
     * - [Idle]: esperando un escaneo.
     * - [Cargando]: consultando la base de datos.
     * - [ProductoNoEncontrado]: el EAN-13 no existe en el catálogo.
     * - [Resultado]: clasificación completada con el resultado.
     * - [Error]: error durante el proceso.
     */
    sealed class ScanUiState {
        object Idle                 : ScanUiState()
        object Cargando             : ScanUiState()
        object ProductoNoEncontrado : ScanUiState()
        data class Resultado(val resultado: ResultadoClasificacion) : ScanUiState()
        data class Error(val mensaje: String) : ScanUiState()
    }

    /**
     * Estado de la sesión de revisión activa.
     *
     * - [SinSesion]: no hay sesión activa, se muestra el diálogo de autenticación.
     * - [Cargando]: verificando el RUT del auxiliar.
     * - [Activa]: sesión iniciada con el nombre del auxiliar autenticado.
     * - [Error]: RUT no encontrado u otro error de autenticación.
     */
    sealed class SesionUiState {
        object SinSesion : SesionUiState()
        object Cargando  : SesionUiState()
        data class Activa(val nombreAuxiliar: String) : SesionUiState()
        data class Error(val mensaje: String) : SesionUiState()
    }

    // ── StateFlows ────────────────────────────────────────────────────────────

    private val _scanUiState = MutableStateFlow<ScanUiState>(ScanUiState.Idle)
    val scanUiState: StateFlow<ScanUiState> = _scanUiState.asStateFlow()

    private val _sesionUiState = MutableStateFlow<SesionUiState>(SesionUiState.SinSesion)
    val sesionUiState: StateFlow<SesionUiState> = _sesionUiState.asStateFlow()

    private val _sesionActivaId = MutableStateFlow<Int?>(null)
    val sesionActivaId: StateFlow<Int?> = _sesionActivaId.asStateFlow()

    private val _totalVigentes = MutableStateFlow(0)
    val totalVigentes: StateFlow<Int> = _totalVigentes.asStateFlow()

    private val _totalCanjeables = MutableStateFlow(0)
    val totalCanjeables: StateFlow<Int> = _totalCanjeables.asStateFlow()

    private val _totalVencidos = MutableStateFlow(0)
    val totalVencidos: StateFlow<Int> = _totalVencidos.asStateFlow()

    /**
     * Código EAN-13 pendiente de confirmación de fecha de vencimiento.
     * Cuando es distinto de null, [ScanScreenContent] muestra [DialogFechaVencimiento].
     * Se limpia tras invocar [confirmarFechaYClasificar] o [resetearEstado].
     */
    private val _ean13Pendiente = MutableStateFlow<String?>(null)
    val ean13Pendiente: StateFlow<String?> = _ean13Pendiente.asStateFlow()

    // Buffer para acumular caracteres enviados por el lector HID
    private val _bufferHid = StringBuilder()

    // ── Sesión ────────────────────────────────────────────────────────────────

    /**
     * Autentica al auxiliar por [rut] e inicia una nueva sesión de revisión.
     * Si el RUT no corresponde a ningún auxiliar registrado, expone un error
     * en [sesionUiState] sin crear la sesión.
     */
    fun iniciarSesion(rut: String) {
        viewModelScope.launch {
            _sesionUiState.value = SesionUiState.Cargando

            var intentos = 0
            var auxiliar = auxiliarRepository.obtenerAuxiliarPorRut(rut)
            while (auxiliar == null && intentos < 5) {
                delay(300)
                auxiliar = auxiliarRepository.obtenerAuxiliarPorRut(rut)
                intentos++
            }

            if (auxiliar == null) {
                delay(300)
                _sesionUiState.value = SesionUiState.Error("RUT no encontrado. Verifique su código.")
                return@launch
            }

            val sesionId = sesionRevisionRepository.iniciarSesion(auxiliar.id)
            _sesionActivaId.value = sesionId
            _sesionUiState.value  = SesionUiState.Activa(auxiliar.nombreAuxiliar)
            resetContadores()
        }
    }

    /**
     * Cierra la sesión activa registrando la fecha de término y los totales
     * acumulados de clasificación. Restablece todos los estados a sus valores
     * iniciales para permitir iniciar una nueva sesión.
     */
    fun cerrarSesion() {
        viewModelScope.launch {
            _sesionActivaId.value?.let { id ->
                sesionRevisionRepository.cerrarSesion(
                    id         = id,
                    vigentes   = _totalVigentes.value,
                    canjeables = _totalCanjeables.value,
                    vencidos   = _totalVencidos.value
                )
                _sesionActivaId.value = null
                _sesionUiState.value  = SesionUiState.SinSesion
                _scanUiState.value    = ScanUiState.Idle
                _ean13Pendiente.value = null
                resetContadores()
            }
        }
    }

    // ── Captura HID ───────────────────────────────────────────────────────────

    /**
     * Acumula caracteres enviados por el lector HID en el buffer interno.
     * Al recibir un retorno de carro o salto de línea, extrae el código
     * completo y lanza el proceso de clasificación.
     */
    fun onCaracterHid(char: Char) {
        if (char == '\n' || char == '\r') {
            val codigo = _bufferHid.toString().trim()
            _bufferHid.clear()
            if (codigo.isNotEmpty()) procesarEAN13(codigo)
        } else {
            _bufferHid.append(char)
        }
    }

    /**
     * Valida y procesa un código EAN-13 ingresado manualmente.
     * Expone [ScanUiState.Error] si el código no tiene exactamente 13 dígitos.
     */
    fun procesarEAN13Manual(ean13: String) {
        if (ean13.length == 13 && ean13.all { it.isDigit() }) {
            procesarEAN13(ean13)
        } else {
            _scanUiState.value = ScanUiState.Error("Código EAN-13 inválido. Debe tener 13 dígitos.")
        }
    }

    /**
     * Verifica que el producto exista en el catálogo y almacena el código
     * EAN-13 en [_ean13Pendiente] para que [ScanScreenContent] muestre el
     * diálogo de confirmación de fecha de vencimiento.
     */
    private fun procesarEAN13(ean13: String) {
        viewModelScope.launch {
            _scanUiState.value = ScanUiState.Cargando

            val producto = productoRepository.buscarPorEAN13(ean13)

            if (producto == null) {
                _scanUiState.value = ScanUiState.ProductoNoEncontrado
                return@launch
            }

            _ean13Pendiente.value = ean13
            _scanUiState.value    = ScanUiState.Idle
        }
    }

    // ── Clasificación ─────────────────────────────────────────────────────────

    /**
     * Clasifica el producto identificado por [ean13] con la [fechaVencimiento]
     * confirmada por el operador. Persiste el resultado en la base de datos,
     * actualiza los contadores de la sesión y limpia el EAN-13 pendiente.
     *
     * @param ean13 Código EAN-13 del producto a clasificar.
     * @param fechaVencimiento Fecha de vencimiento confirmada por el operador.
     */
    fun confirmarFechaYClasificar(ean13: String, fechaVencimiento: LocalDate) {
        viewModelScope.launch {
            _scanUiState.value = ScanUiState.Cargando

            val producto = productoRepository.buscarPorEAN13(ean13) ?: run {
                _scanUiState.value    = ScanUiState.ProductoNoEncontrado
                _ean13Pendiente.value = null
                return@launch
            }

            val politica = politicaCanjeRepository.obtenerPoliticaPorLaboratorio(producto.laboratorioId)
            val resultado = ClasificadorProducto.clasificar(
                producto          = producto,
                nombreLaboratorio = laboratorioRepository.obtenerNombrePorId(producto.laboratorioId)
                    ?: "Laboratorio desconocido",
                fechaVencimiento  = fechaVencimiento,
                politica          = politica
            )

            // Registrar el producto revisado en la sesión activa
            _sesionActivaId.value?.let { sesionId ->
                productoRevisadoRepository.insertar(
                    ProductoRevisado(
                        sesionId      = sesionId,
                        codigoEAN13   = ean13,
                        clasificacion = resultado.clasificacion.name,
                        timestamp     = LocalDateTime.now()
                    )
                )
            }

            // Actualizar contadores y exponer resultado
            actualizarContadores(resultado.clasificacion)
            _ean13Pendiente.value = null
            _scanUiState.value    = ScanUiState.Resultado(resultado)
        }
    }

    /** Restablece el estado del clasificador a [ScanUiState.Idle] para el siguiente escaneo. */
    fun resetearEstado() {
        _scanUiState.value    = ScanUiState.Idle
        _ean13Pendiente.value = null
    }

    // ── Contadores ────────────────────────────────────────────────────────────

    /** Restablece los tres contadores de clasificación a cero. */
    private fun resetContadores() {
        _totalVigentes.value   = 0
        _totalCanjeables.value = 0
        _totalVencidos.value   = 0
    }

    /** Incrementa el contador correspondiente a la [clasificacion] recibida. */
    private fun actualizarContadores(clasificacion: Clasificacion) {
        when (clasificacion) {
            Clasificacion.VIGENTE   -> _totalVigentes.value++
            Clasificacion.CANJEABLE -> _totalCanjeables.value++
            Clasificacion.VENCIDO   -> _totalVencidos.value++
        }
    }
}