package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la entidad [Producto].
 *
 * Provee acceso a la tabla "productos" de la base de datos local.
 * Las operaciones de escritura son suspendidas para ejecutarse fuera
 * del hilo principal. Las consultas reactivas retornan [Flow] y se
 * actualizan automáticamente ante cambios en la tabla.
 */
@Dao
interface ProductoDao {

    /**
     * Retorna el catálogo completo de productos como flujo reactivo.
     * Se actualiza automáticamente cuando la tabla cambia.
     */
    @Query("SELECT * FROM productos")
    fun obtenerTodos(): Flow<List<Producto>>

    /**
     * Retorna el producto cuyo código EAN-13 coincide con [EAN13],
     * o null si no existe. Usado por [ScanViewModel] para identificar
     * el producto escaneado antes de clasificarlo.
     */
    @Query("SELECT * FROM productos WHERE codigoEAN13 = :EAN13")
    suspend fun buscarPorEAN13(EAN13: String): Producto?

    /**
     * Actualiza los datos de un producto existente en la base de datos.
     */
    @Update
    suspend fun actualizar(producto: Producto)

    /**
     * Inserta un producto. Si ya existe un registro con el mismo
     * codigoEAN13, la operación se ignora (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(producto: Producto)

    /**
     * Inserta una lista de productos de forma masiva. Usado en la
     * importación desde CSV en la pantalla de configuración. Los
     * registros duplicados se ignoran (IGNORE).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarProductos(productos: List<Producto>)

    /**
     * Elimina un producto del catálogo.
     */
    @Delete
    suspend fun eliminar(producto: Producto)

    /**
     * Retorna la cantidad total de productos registrados en la tabla.
     * Usado por [OfflineProductoRepository] para calcular el número de
     * registros efectivamente importados tras una operación de carga CSV.
     */
    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contarProductos(): Int
}