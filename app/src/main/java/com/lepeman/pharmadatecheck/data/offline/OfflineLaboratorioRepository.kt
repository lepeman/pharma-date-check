package com.lepeman.pharmadatecheck.data.offline

import android.content.Context
import android.net.Uri
import com.lepeman.pharmadatecheck.data.local.ResultadoImportacion
import com.lepeman.pharmadatecheck.data.local.dao.LaboratorioDao
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline de [LaboratorioRepository].
 *
 * Delega las operaciones CRUD directamente a [LaboratorioDao]. Adicionalmente,
 * implementa la importación masiva desde archivos CSV mediante [insertarLaboratoriosDesdeCSV],
 * que parsea el archivo, inserta los registros válidos e informa el resultado
 * mediante [ResultadoImportacion].
 *
 * @property laboratorioDao DAO para acceder a la tabla "laboratorios".
 */
class OfflineLaboratorioRepository(private val laboratorioDao: LaboratorioDao) : LaboratorioRepository {

    /** Retorna todos los laboratorios como flujo reactivo. */
    override fun obtenerTodos(): Flow<List<Laboratorio>> =
        laboratorioDao.obtenerTodos()

    /** Retorna el nombre del laboratorio con el [id] indicado, o null si no existe. */
    override suspend fun obtenerNombrePorId(id: Int): String? =
        laboratorioDao.obtenerNombreLaboratorio(id)

    /**
     * Retorna como flujo reactivo los laboratorios cuyo nombre contiene
     * [query] como subcadena. Usado por el autocompletado del formulario
     * de políticas de canje.
     */
    override fun buscarItems(query: String): Flow<List<Laboratorio>> =
        laboratorioDao.buscarItems(query)

    /**
     * Retorna el id del laboratorio cuyo nombre coincide exactamente con [nombre].
     * Usado al guardar una política de canje para obtener el identificador
     * a partir del nombre seleccionado en el formulario.
     */
    override suspend fun obtenerIdPorNombre(nombre: String): Int =
        laboratorioDao.obtenerIdPorNombre(nombre)

    /** Actualiza los datos de un laboratorio existente. */
    override suspend fun actualizar(laboratorio: Laboratorio) =
        laboratorioDao.actualizar(laboratorio)

    /** Inserta un nuevo laboratorio. Si ya existe con el mismo id, se ignora. */
    override suspend fun insertar(laboratorio: Laboratorio) =
        laboratorioDao.insertar(laboratorio)

    /**
     * Importa laboratorios desde un archivo CSV seleccionado por el usuario.
     *
     * Parsea el archivo línea por línea mediante [parsearCSV], descartando el
     * encabezado, las líneas vacías y las líneas con formato inválido. Si el
     * archivo no contiene ningún registro válido, retorna [ResultadoImportacion.Error].
     * En caso contrario, inserta los registros válidos y retorna
     * [ResultadoImportacion.Exito] con la cantidad de registros importados
     * y omitidos por duplicidad.
     *
     * Formato esperado del CSV: CODIGO_LABORATORIO;NOMBRE_LABORATORIO
     *
     * @param context Contexto necesario para acceder al archivo mediante
     * el [android.content.ContentResolver].
     * @param uri URI del archivo CSV seleccionado por el usuario.
     */
    override suspend fun insertarLaboratoriosDesdeCSV(context: Context, uri: Uri): ResultadoImportacion {
        val laboratorios = parsearCSV(context, uri)

        if (laboratorios.isEmpty()) {
            return ResultadoImportacion.Error(
                mensaje = "El archivo no contiene laboratorios válidos. " +
                        "Formato esperado: CODIGO_LABORATORIO;LABORATORIO"
            )
        }

        val totalAntes = laboratorioDao.contarLaboratorios()
        laboratorioDao.insertarTodosLosLaboratorios(laboratorios)
        val totalDespues = laboratorioDao.contarLaboratorios()

        val importados = totalDespues - totalAntes
        val omitidos = laboratorios.size - importados

        return ResultadoImportacion.Exito(importados, omitidos)
    }

    /** Elimina un laboratorio de la base de datos. */
    override suspend fun eliminar(laboratorio: Laboratorio) =
        laboratorioDao.eliminar(laboratorio)

    /** Retorna la cantidad total de laboratorios registrados en la tabla. */
    override suspend fun contarLaboratorios(): Int =
        laboratorioDao.contarLaboratorios()

    /**
     * Parsea un archivo CSV y retorna la lista de laboratorios válidos extraídos.
     *
     * Descarta la primera línea (encabezado), las líneas vacías, las líneas
     * con menos de dos campos y aquellas cuyo primer campo no sea un entero
     * válido o cuyo nombre esté en blanco.
     *
     * @param context Contexto para acceder al archivo mediante ContentResolver.
     * @param uri URI del archivo CSV a parsear.
     * @return Lista de [Laboratorio] válidos listos para insertar.
     */
    private fun parsearCSV(context: Context, uri: Uri): List<Laboratorio> {
        val laboratorios = mutableListOf<Laboratorio>()

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().useLines { lineas ->
                lineas.forEachIndexed { index, linea ->
                    if (index == 0 || linea.isBlank()) return@forEachIndexed

                    val campos = linea.split(";").map { it.trim() }
                    if (campos.size < 2) return@forEachIndexed

                    val id = campos[0].toIntOrNull() ?: return@forEachIndexed
                    val nombre = campos[1]
                    if (nombre.isBlank()) return@forEachIndexed

                    laboratorios.add(Laboratorio(id = id, nombre = nombre))
                }
            }
        }

        return laboratorios
    }
}