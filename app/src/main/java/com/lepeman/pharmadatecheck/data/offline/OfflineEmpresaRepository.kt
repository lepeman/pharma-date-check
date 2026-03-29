package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.EmpresaDao
import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import com.lepeman.pharmadatecheck.data.repositories.EmpresaRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline del repositorio para la gestión de la empresa.
 * Utiliza [EmpresaDao] como fuente de datos persistente local.
 *
 * @property empresaDao El DAO para acceder a la tabla de empresas.
 */
class OfflineEmpresaRepository(private val empresaDao: EmpresaDao) : EmpresaRepository {
    
    /**
     * Obtiene un flujo de datos con la lista de todas las empresas.
     */
    override fun obtenerTodas(): Flow<List<Empresa>> = empresaDao.obtenerTodas()

    /**
     * Obtiene el nombre de la empresa por medio del Id
     */
    override fun obtenerNombreEmpresa(id: Int): String = empresaDao.obtenerNombreEmpresa(id)

    /**
     * Obtiene el Id de la empresa por medio del nombre
     */
    override fun obtenerIdPorNombre(razonSocial: String): Int = empresaDao.obtenerIdPorNombre(razonSocial)

    /**
     * Obtiene un flujo de datos con la lista de empresas que coincidan con la consulta.
     */
    override fun buscarItems(query: String): Flow<List<Empresa>> = empresaDao.buscarItems(query)

    /**
     * Actualiza la información de una empresa en la base de datos local.
     */
    override suspend fun actualizar(empresa: Empresa) = empresaDao.actualizar(empresa)

    /**
     * Inserta una nueva empresa en la base de datos local.
     */
    override suspend fun insertar(empresa: Empresa)  = empresaDao.insertar(empresa)

    /**
     * Elimina una empresa de la base de datos local.
     */
    override suspend fun eliminar(empresa: Empresa)  = empresaDao.eliminar(empresa)
}
