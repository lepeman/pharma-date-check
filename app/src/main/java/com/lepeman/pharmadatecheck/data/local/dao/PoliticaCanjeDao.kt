package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de acceso a datos (DAO) para la entidad [PoliticaCanje].
 */
@Dao
interface PoliticaCanjeDao {
    /**
     * Obtiene todas las políticas de canje configuradas.
     */
    @Query("SELECT * FROM politicas_canje")
    fun obtenerTodas(): Flow<List<PoliticaCanje>>

    /**
     * Actualiza una política de canje existente.
     */
    @Update
    suspend fun actualizar(politicaCanje: PoliticaCanje)

    /**
     * Registra una nueva política de canje.
     */
    @Insert
    suspend fun insertar(politicaCanje: PoliticaCanje)

    /**
     * Elimina una política de canje.
     */
    @Delete
    suspend fun eliminar(politicaCanje: PoliticaCanje)
}
