package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
     * Obtiene el nomobre del laboratorio por medio del Id
     */
    @Query("SELECT nombre FROM laboratorios WHERE id = :id")
    fun obtenerNombreLaboratorio(id: Int): String?

    /**
     * Actualiza la información de un laboratorio.
     */
    @Update
    suspend fun actualizar(laboratorio: Laboratorio)

    /**
     * Registra un nuevo laboratorio.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(laboratorio: Laboratorio)

    /**
     * Registra una lista de nuevos laboratorios
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodosLosLaboratorios(list: List<Laboratorio>)

    /**
     * Elimina un laboratorio.
     */
    @Delete
    suspend fun eliminar(laboratorio: Laboratorio)
}
