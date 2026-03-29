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
     * Obtiene el nombre del laboratorio por medio del Id
     */
    @Query("SELECT nombre FROM laboratorios WHERE id = :id")
    fun obtenerNombreLaboratorio(id: Int): String?

    /**
     * Obtiene una lista de items por medio del operador "LIKE"
     */
    @Query("SELECT * FROM laboratorios WHERE nombre LIKE '%' || :query || '%'")
    fun buscarItems(query: String): Flow<List<Laboratorio>>

    /**
     * Obtiene el Id del laboratorio por medio del nombre
     */
    @Query("SELECT id FROM laboratorios WHERE nombre = :nombre")
    suspend fun obtenerIdPorNombre(nombre: String): Int

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
     * Inserta una lista de items en la tabla "laboratorios",
     * si algún item existe, simplemente se ignora
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodosLosLaboratorios(laboratorios: List<Laboratorio>)

    /**
     * Elimina un laboratorio.
     */
    @Delete
    suspend fun eliminar(laboratorio: Laboratorio)

    /**
     * Obtiene la cantidad de items contenidos en la tabla "laboratorios"
     */
    @Query("SELECT COUNT(*) FROM laboratorios")
    suspend fun contarLaboratorios(): Int
}
