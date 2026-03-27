package com.lepeman.pharmadatecheck.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRepository
import com.lepeman.pharmadatecheck.data.local.ResultadoImportacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConfigViewModel(
    private val productoRepository: ProductoRepository,
    private val laboratorioRepository: LaboratorioRepository
) : ViewModel() {

    // ── Estados anidados ──────────────────────────────────────────────────────

    sealed class ImportState {
        object Inactivo : ImportState()
        object Procesando : ImportState()
        data class Exito(val importados: Int, val omitidos: Int) : ImportState()
        data class Error(val mensaje: String) : ImportState()
    }

    // ── StateFlows ────────────────────────────────────────────────────────────

    private val _importStateProductos = MutableStateFlow<ImportState>(ImportState.Inactivo)
    val importStateProductos: StateFlow<ImportState> = _importStateProductos.asStateFlow()

    private val _totalProductos = MutableStateFlow(0)
    val totalProductos: StateFlow<Int> = _totalProductos.asStateFlow()

    // Laboratorios
    private val _importStateLaboratorios = MutableStateFlow<ImportState>(ImportState.Inactivo)
    val importStateLaboratorios: StateFlow<ImportState> = _importStateLaboratorios.asStateFlow()

    private val _totalLaboratorios = MutableStateFlow(0)
    val totalLaboratorios: StateFlow<Int> = _totalLaboratorios.asStateFlow()

    init {
        cargarTotales()
    }

    private fun cargarTotales() {
        viewModelScope.launch {
            _totalProductos.value = productoRepository.contarProductos()
            _totalLaboratorios.value = laboratorioRepository.contarLaboratorios()
        }
    }

    // ── Importación CSV ───────────────────────────────────────────────────────

    fun importarProductosCSV(context: Context, uri: Uri) {
        viewModelScope.launch {
            _importStateProductos.value = ImportState.Procesando

            try {
                val productos = parsearProductosCSV(context, uri)

                if (productos.isEmpty()) {
                    _importStateProductos.value = ImportState.Error(
                        "El archivo no contiene productos válidos.\n" +
                                "Verifique que el formato sea: codigoEAN13,nombre,laboratorio"
                    )
                    return@launch
                }

                val totalAntes = productoRepository.contarProductos()
                productoRepository.insertarProductos(productos)
                val totalDespues = productoRepository.contarProductos()

                val importados = totalDespues - totalAntes
                val omitidos = productos.size - importados

                _totalProductos.value = totalDespues
                _importStateProductos.value = ImportState.Exito(
                    importados = importados,
                    omitidos = omitidos
                )

            } catch (e: Exception) {
                _importStateProductos.value = ImportState.Error(
                    "Error al procesar el archivo: ${e.message}"
                )
            }
        }
    }

    // ── Importación CSV Laboratorios ──────────────────────────────────────────

    fun importarLaboratoriosCSV(context: Context, uri: Uri) {
        viewModelScope.launch {
            _importStateLaboratorios.value = ImportState.Procesando

            try {
                when (val resultado = laboratorioRepository.insertarLaboratoriosDesdeCSV(context, uri)) {
                    is ResultadoImportacion.Exito -> {
                        _totalLaboratorios.value = laboratorioRepository.contarLaboratorios()
                        _importStateLaboratorios.value = ImportState.Exito(
                            importados = resultado.importados,
                            omitidos = resultado.omitidos
                        )
                    }
                    is ResultadoImportacion.Error -> {
                        _importStateLaboratorios.value = ImportState.Error(resultado.mensaje)
                    }
                }
            } catch (e: Exception) {
                _importStateLaboratorios.value = ImportState.Error(
                    "Error al procesar el archivo: ${e.message}"
                )
            }
        }
    }

    // ── Parser CSV ────────────────────────────────────────────────────────────

    private fun parsearProductosCSV(context: Context, uri: Uri): List<Producto> {
        val productos = mutableListOf<Producto>()

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().useLines { lineas ->
                lineas.forEachIndexed { index, linea ->
                    // Saltar encabezado y líneas vacías
                    if (index == 0 || linea.isBlank()) return@forEachIndexed

                    val campos = linea.split(",").map { it.trim() }

                    // Validar que tenga exactamente 3 columnas
                    if (campos.size < 3) return@forEachIndexed

                    val ean13 = campos[0]
                    val nombre = campos[1]
                    val laboratorio = campos[2]

                    // Validar EAN-13: exactamente 13 dígitos
                    if (ean13.length != 13 || !ean13.all { it.isDigit() }) return@forEachIndexed

                    // Validar que nombre y laboratorio no estén vacíos
                    if (nombre.isBlank() || laboratorio.isBlank()) return@forEachIndexed

                    productos.add(
                        Producto(
                            codigoEAN13 = ean13,
                            nombre = nombre,
                            laboratorioId = laboratorio.toInt()
                        )
                    )
                }
            }
        }

        return productos
    }

    // ── Reset de estados ────────────────────────────────────────────────────────────

    fun resetearEstadoProductos() {
        _importStateProductos.value = ImportState.Inactivo
    }

    fun resetearEstadoLaboratorios() {
        _importStateLaboratorios.value = ImportState.Inactivo
    }
}