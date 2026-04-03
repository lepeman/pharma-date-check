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
 * DAO para la entidad [Laboratorio].
 *
 * Provee acceso a la tabla "laboratorios" de la base de datos local.
 * Las operaciones de escritura son suspendidas para ejecutarse fuera
 * del hilo principal. Las consultas reactivas retornan [Flow] y se
 * actualizan automáticamente ante cambios en la tabla.
 */
@Dao
interface LaboratorioDao {

    /**
     * Retorna todos los laboratorios registrados como flujo reactivo.
     * Se actualiza automáticamente cuando la tabla cambia.
     */
    @Query("SELECT * FROM laboratorios")
    fun obtenerTodos(): Flow<List<Laboratorio>>

    /**
     * Retorna el nombre del laboratorio con el [id] indicado, o null
     * si no existe. Usado para resolver el nombre en tarjetas y resultados
     * de clasificación.
     */
    @Query("SELECT nombre FROM laboratorios WHERE id = :id")
    suspend fun obtenerNombreLaboratorio(id: Int): String?

    /**
     * Retorna como flujo reactivo los laboratorios cuyo nombre contiene
     * [query] como subcadena. Usado por el autocompletado del formulario
     * de políticas de canje.
     */
    @Query("SELECT * FROM laboratorios WHERE nombre LIKE '%' || :query || '%'")
    fun buscarItems(query: String): Flow<List<Laboratorio>>

    /**
     * Retorna el id del laboratorio cuyo nombre coincide exactamente
     * con [nombre]. Usado al guardar una política de canje para obtener
     * el identificador a partir del nombre seleccionado en el formulario.
     */
    @Query("SELECT id FROM laboratorios WHERE nombre = :nombre")
    suspend fun obtenerIdPorNombre(nombre: String): Int

    /**
     * Actualiza los datos de un laboratorio existente en la base de datos.
     */
    @Update
    suspend fun actualizar(laboratorio: Laboratorio)

    /**
     * Inserta un laboratorio. Si ya existe un registro con el mismo id,
     * la operación se ignora (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(laboratorio: Laboratorio)

    /**
     * Inserta una lista de laboratorios de forma masiva. Usado tanto en
     * el prepoblado inicial de [AppDatabase] como en la importación desde
     * CSV en la pantalla de configuración. Los registros duplicados se
     * ignoran (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodosLosLaboratorios(laboratorios: List<Laboratorio>)

    /**
     * Elimina un laboratorio de la base de datos.
     */
    @Delete
    suspend fun eliminar(laboratorio: Laboratorio)

    /**
     * Retorna la cantidad total de laboratorios registrados en la tabla.
     * Usado por [OfflineLaboratorioRepository] para calcular el número de
     * registros efectivamente importados tras una operación de carga CSV.
     */
    @Query("SELECT COUNT(*) FROM laboratorios")
    suspend fun contarLaboratorios(): Int
}