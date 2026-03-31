package com.lepeman.pharmadatecheck.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import com.lepeman.pharmadatecheck.data.repositories.AuxiliarRepository
import com.lepeman.pharmadatecheck.data.repositories.EmpresaRepository
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRevisadoRepository
import com.lepeman.pharmadatecheck.data.repositories.SesionRevisionRepository
import com.lepeman.pharmadatecheck.domain.Clasificacion
import com.lepeman.pharmadatecheck.domain.ClasificadorProducto
import com.lepeman.pharmadatecheck.domain.ResultadoClasificacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class ScanViewModel(
    private val auxiliarRepository: AuxiliarRepository,
    private val empresaRepository: EmpresaRepository,
    private val laboratorioRepository: LaboratorioRepository,
    private val productoRepository: ProductoRepository,
    private val politicaCanjeRepository: PoliticaCanjeRepository,
    private val productoRevisadoRepository: ProductoRevisadoRepository,
    private val sesionRevisionRepository: SesionRevisionRepository
) : ViewModel() {

    sealed class ScanUiState {
        object Idle: ScanUiState()
        object Cargando: ScanUiState()
        object ProductoNoEncontrado: ScanUiState()
        data class Resultado(
            val resultado: ResultadoClasificacion
        ) : ScanUiState()
        data class Error(val mensaje: String): ScanUiState()
    }

    sealed class SesionUiState {
        object SinSesion : SesionUiState()
        object Cargando : SesionUiState()
        data class Activa(val nombreAuxiliar: String) : SesionUiState()
        data class Error(val mensaje: String) : SesionUiState()
    }

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

    private val _bufferHid = StringBuilder()

    fun iniciarSesion(rut: String) {
        viewModelScope.launch {
            _sesionUiState.value = SesionUiState.Cargando
            val auxiliar = auxiliarRepository.obtenerAuxiliarPorRut(rut)

            if (auxiliar == null) {
                _sesionUiState.value = SesionUiState.Error("RUT no encontrado. Verifique su código.")
                return@launch
            }
            val sesionId = sesionRevisionRepository.iniciarSesion(auxiliar.id)
            _sesionActivaId.value = sesionId
            _sesionUiState.value = SesionUiState.Activa(auxiliar.nombreAuxiliar)
            resetContadores()
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            _sesionActivaId.value?.let { id ->
                sesionRevisionRepository.cerrarSesion(
                    id = id,
                    vigentes = _totalVigentes.value,
                    canjeables = _totalCanjeables.value,
                    vencidos = _totalVencidos.value
                )
                _sesionActivaId.value = null
                _sesionUiState.value = SesionUiState.SinSesion
                _scanUiState.value = ScanUiState.Idle
                resetContadores()
            }
        }
    }

    fun onCaracterHid(char: Char) {
        if (char == '\n' || char == '\r') {
            val codigo = _bufferHid.toString().trim()
            _bufferHid.clear()
            if (codigo.isNotEmpty()) procesarEAN13(codigo)
        } else {
            _bufferHid.append(char)
        }
    }

    fun procesarEAN13Manual(ean13: String) {
        if (ean13.length == 13 && ean13.all { it.isDigit() }) {
            procesarEAN13(ean13)
        } else {
            _scanUiState.value = ScanUiState.Error("Código EAN-13 inválido. Debe tener 13 dígitos.")
        }
    }

    private fun procesarEAN13(ean13: String) {
        viewModelScope.launch {
            _scanUiState.value = ScanUiState.Cargando

            val producto = productoRepository.buscarPorEAN13(ean13)

            if (producto == null) {
                _scanUiState.value = ScanUiState.ProductoNoEncontrado
                return@launch
            }

            _scanUiState.value = ScanUiState.Resultado(
                ResultadoClasificacion(
                    producto = producto,
                    nombreLaboratorio = laboratorioRepository.obtenerNombrePorId(producto.laboratorioId) ?: "Laboratorio desconocido",
                    fechaVencimiento = LocalDate.now(),
                    clasificacion = Clasificacion.VIGENTE,
                    diasRestantes = 0,
                    fechaLimiteCanje = null
                )
            )
        }
    }

    fun confirmarFechaYClasificar(ean13: String, fechaVencimiento: LocalDate) {
        viewModelScope.launch {
            _scanUiState.value = ScanUiState.Cargando
            val producto = productoRepository.buscarPorEAN13(ean13) ?: run {
                _scanUiState.value = ScanUiState.ProductoNoEncontrado
                return@launch
            }
            val politica = politicaCanjeRepository.obtenerPoliticaPorLaboratorio(producto.laboratorioId)
            val resultado = ClasificadorProducto.clasificar(
                producto = producto,
                nombreLaboratorio = laboratorioRepository.obtenerNombrePorId(producto.laboratorioId) ?: "Laboratorio desconocido",
                fechaVencimiento = fechaVencimiento,
                politica = politica
            )

            _scanUiState.value = ScanUiState.Resultado(resultado)
        }
    }

    fun resetearEstado() {
        _scanUiState.value = ScanUiState.Idle
    }

    private fun resetContadores() {
        _totalVigentes.value = 0
        _totalCanjeables.value = 0
        _totalVencidos.value = 0
    }

    private fun actualizarContadores(clasificacion: Clasificacion) {
        when (clasificacion) {
            Clasificacion.VIGENTE   -> _totalVigentes.value++
            Clasificacion.CANJEABLE -> _totalCanjeables.value++
            Clasificacion.VENCIDO   -> _totalVencidos.value++
        }
    }

}