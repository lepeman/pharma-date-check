package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.PoliticaCanjeDao
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline del repositorio para la gestión de las políticas de canje.
 * Utiliza [PoliticaCanjeDao] como fuente de datos persistente local.
 *
 * @property politicaCanjeDao El DAO para acceder a la tabla de políticas de canje.
 */
class OfflinePoliticaCanjeRepository(private val politicaCanjeDao: PoliticaCanjeDao) : PoliticaCanjeRepository {
    
    /**
     * Obtiene un flujo de datos con la lista de todas las políticas de canje.
     */
    override fun obtenerTodas(): Flow<List<PoliticaCanje>> = politicaCanjeDao.obtenerTodas()

    /**
     * Actualiza la información de una política de canje en la base de datos local.
     */
    override suspend fun actualizar(politicaCanje: PoliticaCanje) = politicaCanjeDao.actualizar(politicaCanje)

    /**
     * Inserta una nueva política de canje en la base de datos local.
     */
    override suspend fun insertar(politicaCanje: PoliticaCanje) = politicaCanjeDao.insertar(politicaCanje)

    /**
     * Elimina una política de canje de la base de datos local.
     */
    override suspend fun eliminar(politicaCanje: PoliticaCanje) = politicaCanjeDao.eliminar(politicaCanje)
}
