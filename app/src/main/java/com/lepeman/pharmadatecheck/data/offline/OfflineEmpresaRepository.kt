package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.EmpresaDao
import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import com.lepeman.pharmadatecheck.data.repositories.EmpresaRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline de [EmpresaRepository].
 *
 * Delega todas las operaciones directamente a [EmpresaDao], que accede
 * a la tabla "empresas" de la base de datos local Room. Al ser una
 * implementación offline, no realiza llamadas a servicios remotos ni
 * lógica adicional más allá de la delegación al DAO.
 *
 * @property empresaDao DAO para acceder a la tabla "empresas".
 */
class OfflineEmpresaRepository(private val empresaDao: EmpresaDao) : EmpresaRepository {

    /** Retorna todas las empresas como flujo reactivo. */
    override fun obtenerTodas(): Flow<List<Empresa>> =
        empresaDao.obtenerTodas()

    /** Retorna la razón social de la empresa con el [id] indicado. */
    override suspend fun obtenerNombreEmpresa(id: Int): String =
        empresaDao.obtenerNombreEmpresa(id)

    /**
     * Retorna el id de la empresa cuya razón social coincide exactamente
     * con [razonSocial], o null si no existe.
     */
    override suspend fun obtenerIdPorNombre(razonSocial: String): Int? =
        empresaDao.obtenerIdPorNombre(razonSocial)

    /**
     * Retorna como flujo reactivo las empresas cuya razón social contiene
     * [query] como subcadena. Usado por el autocompletado del formulario
     * de políticas de canje.
     */
    override fun buscarItems(query: String): Flow<List<Empresa>> =
        empresaDao.buscarItems(query)

    /** Actualiza los datos de una empresa existente. */
    override suspend fun actualizar(empresa: Empresa) =
        empresaDao.actualizar(empresa)

    /** Inserta una nueva empresa. Si ya existe con el mismo id, se ignora. */
    override suspend fun insertar(empresa: Empresa) =
        empresaDao.insertar(empresa)

    /** Elimina una empresa de la base de datos. */
    override suspend fun eliminar(empresa: Empresa) =
        empresaDao.eliminar(empresa)
}