package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de acceso a datos (DAO) para la entidad [Empresa].
 */
@Dao
interface EmpresaDao {
    /**
     * Obtiene la información de las empresas registradas.
     */
    @Query("SELECT * FROM empresas")
    fun obtenerTodas(): Flow<List<Empresa>>

    /**
     * Actualiza los datos de una empresa.
     */
    @Update
    suspend fun actualizar(empresa: Empresa)

    /**
     * Registra una nueva empresa.
     */
    @Insert
    suspend fun insertar(empresa: Empresa)

    /**
     * Elimina una empresa del sistema.
     */
    @Delete
    suspend fun eliminar(empresa: Empresa)
}
