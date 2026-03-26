package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.AuxiliarDao
import com.lepeman.pharmadatecheck.data.local.entities.Auxiliar
import com.lepeman.pharmadatecheck.data.repositories.AuxiliarRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline del repositorio para la gestión de auxiliares.
 * Utiliza [AuxiliarDao] como fuente de datos persistente local.
 *
 * @property auxiliarDao El DAO para acceder a la tabla de auxiliares.
 */
class OfflineAuxiliarRepository(private val auxiliarDao: AuxiliarDao) : AuxiliarRepository {
    
    /**
     * Obtiene un flujo de datos con la lista de todos los auxiliares.
     */
    override fun obtenerTodosStream(): Flow<List<Auxiliar>> = auxiliarDao.obtenerTodos()

    /**
     * Obtiene un auxiliar por medio del Id
     */
    override suspend fun obtenerAuxiliarPorId(id: Int): Auxiliar? = auxiliarDao.obtenerAuxiliar(id)

    /**
     * Obtiene un auxiliar por medio del rut
     */
    override suspend fun obtenerAuxiliarPorRut(rut: String): Auxiliar? = auxiliarDao.obtenerAuxiliarPorRut(rut)

    /**
     * Obtiene el nombre del auxiliar por el Id
     */
    override suspend fun obtenerNombrePorId(id: Int): String? = auxiliarDao.obtenerNombrePorId(id)

    /**
     * Actualiza la información de un auxiliar en la base de datos local.
     */
    override suspend fun actualizarStream(auxiliar: Auxiliar) = auxiliarDao.actualizar(auxiliar)

    /**
     * Inserta un nuevo auxiliar en la base de datos local.
     */
    override suspend fun insertar(auxiliar: Auxiliar) = auxiliarDao.insertar(auxiliar)

    /**
     * Elimina un auxiliar de la base de datos local.
     */
    override suspend fun eliminar(auxiliar: Auxiliar) = auxiliarDao.eliminar(auxiliar)
}
