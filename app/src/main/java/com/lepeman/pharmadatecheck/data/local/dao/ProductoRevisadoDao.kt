package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la entidad [ProductoRevisado].
 *
 * Provee acceso a la tabla "productos_revisados" de la base de datos local.
 * Las operaciones de escritura son suspendidas para ejecutarse fuera
 * del hilo principal. Las consultas reactivas retornan [Flow] y se
 * actualizan automáticamente ante cambios en la tabla.
 */
@Dao
interface ProductoRevisadoDao {

    /**
     * Retorna el historial completo de productos revisados como flujo reactivo.
     * Se actualiza automáticamente cuando la tabla cambia.
     */
    @Query("SELECT * FROM productos_revisados")
    fun obtenerTodos(): Flow<List<ProductoRevisado>>

    /**
     * Retorna como flujo reactivo todos los productos revisados durante
     * la sesión con el [sesionId] indicado. Usado por [HistorialViewModel]
     * para cargar el detalle de una sesión seleccionada.
     */
    @Query("SELECT * FROM productos_revisados WHERE sesionId = :sesionId")
    fun obtenerPorSesion(sesionId: Int): Flow<List<ProductoRevisado>>

    /**
     * Actualiza un registro de revisión existente en la base de datos.
     */
    @Update
    suspend fun actualizar(productoRevisado: ProductoRevisado)

    /**
     * Inserta un registro de revisión de producto. Si ya existe un registro
     * con el mismo id, la operación se ignora (IGNORE). Dado que el id es
     * autoGenerado, los conflictos no deberían ocurrir en condiciones normales.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(productoRevisado: ProductoRevisado)

    /**
     * Elimina un registro de revisión de producto de la base de datos.
     */
    @Delete
    suspend fun eliminar(productoRevisado: ProductoRevisado)
}