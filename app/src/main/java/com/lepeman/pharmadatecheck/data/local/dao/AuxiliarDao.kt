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
 * DAO para la entidad [Auxiliar].
 *
 * Provee acceso a la tabla "auxiliares" de la base de datos local.
 * Las operaciones de escritura son suspendidas para ejecutarse fuera
 * del hilo principal. Las consultas reactivas retornan [Flow] y se
 * actualizan automáticamente ante cambios en la tabla.
 */
@Dao
interface AuxiliarDao {

    /**
     * Retorna todos los auxiliares registrados como flujo reactivo.
     * Se actualiza automáticamente cuando la tabla cambia.
     */
    @Query("SELECT * FROM auxiliares")
    fun obtenerTodos(): Flow<List<Auxiliar>>

    /**
     * Retorna el auxiliar con el [id] indicado, o null si no existe.
     */
    @Query("SELECT * FROM auxiliares WHERE id = :id")
    suspend fun obtenerAuxiliar(id: Int): Auxiliar?

    /**
     * Retorna el auxiliar cuyo RUT coincide con [rut], o null si no existe.
     * Usado por el mecanismo de autenticación al inicio de cada sesión.
     */
    @Query("SELECT * FROM auxiliares WHERE rutAuxiliar = :rut")
    suspend fun obtenerAuxiliarPorRut(rut: String): Auxiliar?

    /**
     * Retorna el nombre completo del auxiliar con el [id] indicado,
     * o null si no existe. Usado por [HistorialViewModel] para resolver
     * el nombre a mostrar en las tarjetas de sesión.
     */
    @Query("SELECT nombreAuxiliar FROM auxiliares WHERE id = :id")
    suspend fun obtenerNombrePorId(id: Int): String?

    /**
     * Actualiza los datos de un auxiliar existente en la base de datos.
     */
    @Update
    suspend fun actualizar(auxiliar: Auxiliar)

    /**
     * Inserta un auxiliar. Si ya existe un registro con el mismo id,
     * la operación se ignora (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(auxiliar: Auxiliar)

    /**
     * Inserta una lista de auxiliares de forma masiva. Usado durante
     * el prepoblado inicial de la base de datos en [AppDatabase].
     * Los registros duplicados se ignoran (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodosLosAuxiliares(list: List<Auxiliar>)

    /**
     * Elimina un auxiliar de la base de datos.
     */
    @Delete
    suspend fun eliminar(auxiliar: Auxiliar)
}