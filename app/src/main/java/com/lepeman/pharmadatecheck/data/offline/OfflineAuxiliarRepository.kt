package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.AuxiliarDao
import com.lepeman.pharmadatecheck.data.local.entities.Auxiliar
import com.lepeman.pharmadatecheck.data.repositories.AuxiliarRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline de [AuxiliarRepository].
 *
 * Delega todas las operaciones directamente a [AuxiliarDao], que accede
 * a la tabla "auxiliares" de la base de datos local Room. Al ser una
 * implementación offline, no realiza llamadas a servicios remotos ni
 * lógica adicional más allá de la delegación al DAO.
 *
 * @property auxiliarDao DAO para acceder a la tabla "auxiliares".
 */
class OfflineAuxiliarRepository(private val auxiliarDao: AuxiliarDao) : AuxiliarRepository {

    /** Retorna todos los auxiliares como flujo reactivo. */
    override fun obtenerTodos(): Flow<List<Auxiliar>> =
        auxiliarDao.obtenerTodos()

    /** Retorna el auxiliar con el [id] indicado, o null si no existe. */
    override suspend fun obtenerAuxiliarPorId(id: Int): Auxiliar? =
        auxiliarDao.obtenerAuxiliar(id)

    /** Retorna el auxiliar cuyo RUT coincide con [rut], o null si no existe. */
    override suspend fun obtenerAuxiliarPorRut(rut: String): Auxiliar? =
        auxiliarDao.obtenerAuxiliarPorRut(rut)

    /** Retorna el nombre completo del auxiliar con el [id] indicado, o null si no existe. */
    override suspend fun obtenerNombrePorId(id: Int): String? =
        auxiliarDao.obtenerNombrePorId(id)

    /** Actualiza los datos de un auxiliar existente. */
    override suspend fun actualizar(auxiliar: Auxiliar) =
        auxiliarDao.actualizar(auxiliar)

    /** Inserta un nuevo auxiliar. Si ya existe con el mismo id, se ignora. */
    override suspend fun insertar(auxiliar: Auxiliar) =
        auxiliarDao.insertar(auxiliar)

    /** Elimina un auxiliar de la base de datos. */
    override suspend fun eliminar(auxiliar: Auxiliar) =
        auxiliarDao.eliminar(auxiliar)
}