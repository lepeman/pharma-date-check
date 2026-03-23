package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de acceso a datos (DAO) para la gestión de las sesiones de revisión [SesionRevision].
 */
@Dao
interface SesionRevisionDao {
    /**
     * Obtiene el listado de todas las sesiones de revisión realizadas.
     */
    @Query("SELECT * FROM sesiones_revision")
    fun obtenerTodas(): Flow<List<SesionRevision>>

    /**
     * Se obtiene una sesión por medio del Id
     */
    @Query("SELECT * FROM sesiones_revision WHERE id = :id")
    suspend fun buscarPorId(id: Int): SesionRevision?

    /**
     * Actualiza la información de una sesión de revisión.
     */
    @Update
    suspend fun actualizar(sesionRevision: SesionRevision)

    /**
     * Registra una nueva sesión de revisión.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(sesionRevision: SesionRevision): Long

    /**
     * Elimina una sesión de revisión de la base de datos.
     */
    @Delete
    suspend fun eliminar(sesionRevision: SesionRevision)
}
