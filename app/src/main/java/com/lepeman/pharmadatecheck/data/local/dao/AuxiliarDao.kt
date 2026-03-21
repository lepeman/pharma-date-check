package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lepeman.pharmadatecheck.data.local.entities.Auxiliar
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de acceso a datos (DAO) para la entidad [Auxiliar].
 * Proporciona métodos para interactuar con la tabla 'auxiliares' en la base de datos.
 */
@Dao
interface AuxiliarDao {
    /**
     * Obtiene todos los auxiliares registrados en un flujo de datos reactivo.
     */
    @Query("SELECT * FROM auxiliares")
    fun obtenerTodos(): Flow<List<Auxiliar>>

    /**
     * Actualiza la información de un auxiliar existente.
     */
    @Update
    suspend fun actualizar(auxiliar: Auxiliar)

    /**
     * Inserta un nuevo auxiliar en la base de datos.
     */
    @Insert
    suspend fun insertar(auxiliar: Auxiliar)

    /**
     * Elimina un auxiliar de la base de datos.
     */
    @Delete
    suspend fun eliminar(auxiliar: Auxiliar)
}
