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
 * DAO para la entidad [SesionRevision].
 *
 * Provee acceso a la tabla "sesiones_revision" de la base de datos local.
 * Las operaciones de escritura son suspendidas para ejecutarse fuera
 * del hilo principal. Las consultas reactivas retornan [Flow] y se
 * actualizan automáticamente ante cambios en la tabla.
 */
@Dao
interface SesionRevisionDao {

    /**
     * Retorna todas las sesiones de revisión registradas como flujo reactivo.
     * Se actualiza automáticamente cuando la tabla cambia. Usado por
     * [HistorialViewModel] para mantener la lista de sesiones sincronizada
     * con la base de datos.
     */
    @Query("SELECT * FROM sesiones_revision")
    fun obtenerTodas(): Flow<List<SesionRevision>>

    /**
     * Retorna la sesión con el [id] indicado, o null si no existe.
     * Usado por [SesionRevisionRepository] para recuperar la sesión activa
     * antes de actualizarla al cierre.
     */
    @Query("SELECT * FROM sesiones_revision WHERE id = :id")
    suspend fun buscarPorId(id: Int): SesionRevision?

    /**
     * Actualiza los datos de una sesión existente en la base de datos.
     * Usado al cerrar una sesión para registrar la fecha de término
     * y los totales acumulados de clasificación.
     */
    @Update
    suspend fun actualizar(sesionRevision: SesionRevision)

    /**
     * Inserta una nueva sesión de revisión y retorna su id autoGenerado.
     * El id retornado es almacenado por [ScanViewModel] para asociar
     * cada producto revisado a la sesión activa. Si ya existe una sesión
     * con el mismo id, la operación se ignora (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(sesionRevision: SesionRevision): Long

    /**
     * Elimina una sesión de revisión de la base de datos. Dado que
     * SesionRevision tiene relación CASCADE con ProductoRevisado, esta
     * operación elimina también todos los productos revisados asociados
     * a la sesión.
     */
    @Delete
    suspend fun eliminar(sesionRevision: SesionRevision)
}