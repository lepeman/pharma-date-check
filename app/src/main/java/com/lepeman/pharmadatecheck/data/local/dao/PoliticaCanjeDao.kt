package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la entidad [PoliticaCanje].
 *
 * Provee acceso a la tabla "politicas_canje" de la base de datos local.
 * Las operaciones de escritura son suspendidas para ejecutarse fuera
 * del hilo principal. Las consultas reactivas retornan [Flow] y se
 * actualizan automáticamente ante cambios en la tabla.
 */
@Dao
interface PoliticaCanjeDao {

    /**
     * Retorna todas las políticas de canje configuradas como flujo reactivo.
     * Se actualiza automáticamente cuando la tabla cambia.
     */
    @Query("SELECT * FROM politicas_canje")
    fun obtenerTodas(): Flow<List<PoliticaCanje>>

    /**
     * Retorna la política de canje asociada al laboratorio con el
     * [laboratorioId] indicado. Usado por el motor de clasificación
     * para determinar si un producto escaneado es elegible para canje.
     */
    @Query("SELECT * FROM politicas_canje WHERE laboratorioId = :laboratorioId")
    suspend fun obtenerPoliticaPorLaboratorio(laboratorioId: Int): PoliticaCanje?

    /**
     * Actualiza una política de canje existente en la base de datos.
     */
    @Update
    suspend fun actualizar(politicaCanje: PoliticaCanje)

    /**
     * Inserta una política de canje. Si ya existe un registro con el mismo
     * id, la operación se ignora (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(politicaCanje: PoliticaCanje)

    /**
     * Inserta una lista de políticas de canje de forma masiva. Usado en la
     * importación desde CSV en la pantalla de configuración. Los registros
     * duplicados se ignoran (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodasLasPoliticas(list: List<PoliticaCanje>)

    /**
     * Elimina una política de canje de la base de datos.
     */
    @Delete
    suspend fun eliminar(politicaCanje: PoliticaCanje)

    /**
     * Elimina la política de canje con el [id] indicado.
     * Usado desde [CanjeViewModel] al confirmar la eliminación
     * de una política desde la pantalla de gestión de canjes.
     */
    @Query("DELETE FROM politicas_canje WHERE id = :id")
    suspend fun eliminarPorId(id: Int)
}
