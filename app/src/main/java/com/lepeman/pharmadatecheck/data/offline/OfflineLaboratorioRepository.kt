package com.lepeman.pharmadatecheck.data.offline

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
     * Actualiza la información de un laboratorio en la base de datos local.
     */
    override suspend fun actualizar(laboratorio: Laboratorio) = laboratorioDao.actualizar(laboratorio)

    /**
     * Inserta un nuevo laboratorio en la base de datos local.
     */
    override suspend fun insertar(laboratorio: Laboratorio) = laboratorioDao.insertar(laboratorio)

    /**
     * Elimina un laboratorio de la base de datos local.
     */
    override suspend fun eliminar(laboratorio: Laboratorio) = laboratorioDao.eliminar(laboratorio)
}
