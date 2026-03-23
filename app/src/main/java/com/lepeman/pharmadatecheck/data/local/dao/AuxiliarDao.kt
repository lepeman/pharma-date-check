package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
     * Obtiene un auxiliar por medio del Id
     */
    @Query("SELECT * FROM auxiliares WHERE id = :id")
    suspend fun obtenerAuxiliar(id: Int): Auxiliar?

    /**
     * Obtiene un auxiliar por medio del RUT
     */
    @Query("SELECT * FROM auxiliares WHERE rutAuxiliar = :rut")
    suspend fun obtenerAuxiliarPorRut(rut: String): Auxiliar?

    /**
     * Actualiza la información de un auxiliar existente.
     */
    @Update
    suspend fun actualizar(auxiliar: Auxiliar)

    /**
     * Inserta un nuevo auxiliar en la base de datos.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(auxiliar: Auxiliar)

    /**
     * Registra una lista de nuevos auxiliares
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodosLosAuxiliares(list: List<Auxiliar>)

    /**
     * Elimina un auxiliar de la base de datos.
     */
    @Delete
    suspend fun eliminar(auxiliar: Auxiliar)
}
