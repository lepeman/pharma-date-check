package com.lepeman.pharmadatecheck.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lepeman.pharmadatecheck.data.local.ResultadoImportacion
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de configuración.
 *
 * Gestiona la importación masiva de productos y laboratorios desde archivos
 * CSV, exponiendo el estado de cada operación mediante [ImportState] y los
 * totales actuales del catálogo. Cada importación sigue el mismo flujo:
 * Inactivo → Procesando → Exito | Error.
 *
 * @param productoRepository Repositorio del catálogo de productos.
 * @param laboratorioRepository Repositorio del catálogo de laboratorios.
 */
class ConfigViewModel(
    private val productoRepository: ProductoRepository,
    private val laboratorioRepository: LaboratorioRepository
) : ViewModel() {

    // ── Estado de importación ─────────────────────────────────────────────────

    /**
     * Estado de una operación de importación desde CSV.
     *
     * - [Inactivo]: estado inicial, muestra el botón de selección de archivo.
     * - [Procesando]: la importación está en curso.
     * - [Exito]: la importación completó con los contadores de registros
     *   importados y omitidos.
     * - [Error]: la importación falló con un mensaje descriptivo.
     */
    sealed class ImportState {
        object Inactivo   : ImportState()
        object Procesando : ImportState()
        data class Exito(val importados: Int, val omitidos: Int) : ImportState()
        data class Error(val mensaje: String) : ImportState()
    }

    // ── StateFlows ────────────────────────────────────────────────────────────

    private val _importStateProductos = MutableStateFlow<ImportState>(ImportState.Inactivo)
    val importStateProductos: StateFlow<ImportState> = _importStateProductos.asStateFlow()

    private val _totalProductos = MutableStateFlow(0)
    val totalProductos: StateFlow<Int> = _totalProductos.asStateFlow()

    private val _importStateLaboratorios = MutableStateFlow<ImportState>(ImportState.Inactivo)
    val importStateLaboratorios: StateFlow<ImportState> = _importStateLaboratorios.asStateFlow()

    private val _totalLaboratorios = MutableStateFlow(0)
    val totalLaboratorios: StateFlow<Int> = _totalLaboratorios.asStateFlow()

    init {
        cargarTotales()
    }

    /**
     * Carga los totales actuales de productos y laboratorios desde la base
     * de datos para mostrarlos en la tarjeta de resumen del catálogo.
     */
    private fun cargarTotales() {
        viewModelScope.launch {
            _totalProductos.value    = productoRepository.contarProductos()
            _totalLaboratorios.value = laboratorioRepository.contarLaboratorios()
        }
    }

    // ── Importación de productos ──────────────────────────────────────────────

    /**
     * Importa productos desde un archivo CSV seleccionado por el usuario.
     *
     * Parsea el archivo mediante [parsearProductosCSV], valida que contenga
     * registros válidos e inserta los productos en la base de datos. Al
     * finalizar actualiza el total del catálogo y expone el resultado
     * mediante [importStateProductos].
     *
     * Formato esperado del CSV: CODIGO_EAN13;NOMBRE;COD_LABORATORIO
     *
     * @param context Contexto para acceder al archivo mediante ContentResolver.
     * @param uri URI del archivo CSV seleccionado por el usuario.
     */
    fun importarProductosCSV(context: Context, uri: Uri) {
        viewModelScope.launch {
            _importStateProductos.value = ImportState.Procesando
            try {
                val productos = parsearProductosCSV(context, uri)

                if (productos.isEmpty()) {
                    _importStateProductos.value = ImportState.Error(
                        "El archivo no contiene productos válidos.\n" +
                                "Verifique que el formato sea: codigoEAN13;nombre;laboratorio"
                    )
                    return@launch
                }

                val totalAntes   = productoRepository.contarProductos()
                productoRepository.insertarProductos(productos)
                val totalDespues = productoRepository.contarProductos()

                val importados = totalDespues - totalAntes
                val omitidos   = productos.size - importados

                _totalProductos.value       = totalDespues
                _importStateProductos.value = ImportState.Exito(importados, omitidos)

            } catch (e: Exception) {
                _importStateProductos.value = ImportState.Error(
                    "Error al procesar el archivo: ${e.message}"
                )
            }
        }
    }

    // ── Importación de laboratorios ───────────────────────────────────────────

    /**
     * Importa laboratorios desde un archivo CSV seleccionado por el usuario.
     *
     * Delega el parseo y la inserción a [LaboratorioRepository.insertarLaboratoriosDesdeCSV]
     * y traduce el [ResultadoImportacion] al estado [ImportState] correspondiente.
     *
     * Formato esperado del CSV: CODIGO_LABORATORIO;NOMBRE_LABORATORIO
     *
     * @param context Contexto para acceder al archivo mediante ContentResolver.
     * @param uri URI del archivo CSV seleccionado por el usuario.
     */
    fun importarLaboratoriosCSV(context: Context, uri: Uri) {
        viewModelScope.launch {
            _importStateLaboratorios.value = ImportState.Procesando
            try {
                when (val resultado = laboratorioRepository.insertarLaboratoriosDesdeCSV(context, uri)) {
                    is ResultadoImportacion.Exito -> {
                        _totalLaboratorios.value       = laboratorioRepository.contarLaboratorios()
                        _importStateLaboratorios.value = ImportState.Exito(
                            importados = resultado.importados,
                            omitidos   = resultado.omitidos
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

    // ── Parser CSV de productos ───────────────────────────────────────────────

    /**
     * Parsea un archivo CSV y retorna la lista de productos válidos extraídos.
     *
     * Descarta la primera línea (encabezado), las líneas vacías, las líneas
     * con menos de tres campos, los códigos EAN-13 con formato inválido,
     * los campos de nombre o laboratorio en blanco, y los códigos de
     * laboratorio que no sean enteros válidos.
     *
     * @param context Contexto para acceder al archivo mediante ContentResolver.
     * @param uri URI del archivo CSV a parsear.
     * @return Lista de [Producto] válidos listos para insertar.
     */
    private fun parsearProductosCSV(context: Context, uri: Uri): List<Producto> {
        val productos = mutableListOf<Producto>()

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().useLines { lineas ->
                lineas.forEachIndexed { index, linea ->
                    if (index == 0 || linea.isBlank()) return@forEachIndexed

                    val campos = linea.split(";").map { it.trim() }
                    if (campos.size < 3) return@forEachIndexed

                    val ean13       = campos[0]
                    val nombre      = campos[1]
                    val laboratorio = campos[2]

                    if (ean13.length != 13 || !ean13.all { it.isDigit() }) return@forEachIndexed
                    if (nombre.isBlank() || laboratorio.isBlank()) return@forEachIndexed

                    val laboratorioId = laboratorio.toIntOrNull() ?: return@forEachIndexed

                    productos.add(Producto(
                        codigoEAN13   = ean13,
                        nombre        = nombre,
                        laboratorioId = laboratorioId
                    ))
                }
            }
        }

        return productos
    }

    // ── Reset de estados ──────────────────────────────────────────────────────

    /** Restablece el estado de importación de productos a [ImportState.Inactivo]. */
    fun resetearEstadoProductos() {
        _importStateProductos.value = ImportState.Inactivo
    }

    /** Restablece el estado de importación de laboratorios a [ImportState.Inactivo]. */
    fun resetearEstadoLaboratorios() {
        _importStateLaboratorios.value = ImportState.Inactivo
    }
}