package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de acceso a datos (DAO) para la entidad [Laboratorio].
 */
@Dao
interface LaboratorioDao {
    /**
     * Obtiene todos los laboratorios registrados.
     */
    @Query("SELECT * FROM laboratorios")
    fun obtenerTodos(): Flow<List<Laboratorio>>

    /**
     * Actualiza la información de un laboratorio.
     */
    @Update
    suspend fun actualizar(laboratorio: Laboratorio)

    /**
     * Registra un nuevo laboratorio.
     */
    @Insert
    suspend fun insertar(laboratorio: Laboratorio)

    /**
     * Elimina un laboratorio.
     */
    @Delete
    suspend fun eliminar(laboratorio: Laboratorio)
}
