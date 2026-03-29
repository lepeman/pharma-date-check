package com.lepeman.pharmadatecheck.data.offline

import android.content.Context
import android.net.Uri
import com.lepeman.pharmadatecheck.data.local.ResultadoImportacion
import com.lepeman.pharmadatecheck.data.local.dao.LaboratorioDao
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline del repositorio para la gestión de laboratorios.
 * Utiliza [LaboratorioDao] como fuente de datos persistente local.
 *
 * @property laboratorioDao El DAO para acceder a la tabla de laboratorios.
 */
class OfflineLaboratorioRepository(private val laboratorioDao: LaboratorioDao) : LaboratorioRepository {
    
    /**
     * Obtiene un flujo de datos con la lista de todos los laboratorios.
     */
    override fun obtenerTodos(): Flow<List<Laboratorio>> = laboratorioDao.obtenerTodos()

    /**
     * Obtiene un String correspondiente al nombre del laboratorio por medio del Id
     */
    override fun obtenerNombrePorId(id: Int): String? = laboratorioDao.obtenerNombreLaboratorio(id)

    /**
     * Obtiene una lista de items de la tabla "laboratorios" por medio del operador "LIKE"
     */
    override fun buscarItems(query: String): Flow<List<Laboratorio>> = laboratorioDao.buscarItems(query)

    /**
     * Obtiene el Id del laboratorio por medio del nombre
     */
    override suspend fun obtenerIdPorNombre(nombre: String): Int = laboratorioDao.obtenerIdPorNombre(nombre)

    /**
     * Actualiza la información de un laboratorio en la base de datos local.
     */
    override suspend fun actualizar(laboratorio: Laboratorio) = laboratorioDao.actualizar(laboratorio)

    /**
     * Inserta un nuevo laboratorio en la base de datos local.
     */
    override suspend fun insertar(laboratorio: Laboratorio) = laboratorioDao.insertar(laboratorio)

    /**
     * Inserta una lista de laboratorios en la tabla "laboratorios"
     */
    override suspend fun insertarLaboratoriosDesdeCSV(context: Context, uri: Uri): ResultadoImportacion {
        val laboratorios = parsearCSV(context, uri)

        if (laboratorios.isEmpty()) {
            return ResultadoImportacion.Error(
                mensaje = "El archivo no contiene laboratorios válidos." +
                " Formato esperado: CODIGO_LABORATORIO; LABORATORIO"
            )
        }

        val totalAntes = laboratorioDao.contarLaboratorios()
        laboratorioDao.insertarTodosLosLaboratorios(laboratorios)
        val totalDespues = laboratorioDao.contarLaboratorios()

        val importados = totalDespues -totalAntes
        val omitidos = laboratorios.size - importados

        return ResultadoImportacion.Exito(importados, omitidos)
    }

    /**
     * Elimina un laboratorio de la base de datos local.
     */
    override suspend fun eliminar(laboratorio: Laboratorio) = laboratorioDao.eliminar(laboratorio)

    /**
     * Obtiene la cantidad de items contenidos en la tabla "laboratorios"
     */
    override suspend fun contarLaboratorios(): Int = laboratorioDao.contarLaboratorios()

    /**
     * Función privada que nos ayuda a parsear los items para la inserción de nuevos laboratorios
     */
    private fun parsearCSV(context: Context, uri: Uri): List<Laboratorio> {
        val laboratorios = mutableListOf<Laboratorio>()

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().useLines { lineas ->
                lineas.forEachIndexed { index, linea ->
                    // Saltamos encabezado y líneas que estén vacías
                    if (index == 0 || linea.isBlank()) return@forEachIndexed

                    val campos = linea.split(";").map { it.trim() }

                    if (campos.size < 2) return@forEachIndexed

                    val id = campos[0].toIntOrNull() ?: return@forEachIndexed
                    val nombre = campos[1]

                    if (nombre.isBlank()) return@forEachIndexed

                    laboratorios.add(
                        Laboratorio(
                            id = id,
                            nombre = nombre
                        )
                    )
                }
            }
        }

        return laboratorios
    }
}
