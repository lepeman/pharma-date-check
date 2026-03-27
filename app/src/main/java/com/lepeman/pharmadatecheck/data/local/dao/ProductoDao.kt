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
 * Interfaz de acceso a datos (DAO) para la entidad [Producto].
 */
@Dao
interface ProductoDao {
    /**
     * Obtiene el listado completo de productos en un flujo reactivo.
     */
    @Query("SELECT * FROM productos")
    fun obtenerTodos(): Flow<List<Producto>>

    /**
     * Se obtiene un producto por medio de su código EAN13
     */
    @Query("SELECT * FROM productos WHERE codigoEAN13 = :EAN13")
    suspend fun buscarPorEAN13(EAN13: String): Producto?

    /**
     * Actualiza la información de un producto.
     */
    @Update
    suspend fun actualizar(producto: Producto)

    /**
     * Inserta un nuevo producto en el catálogo.
     */
    @Insert
    suspend fun insertar(producto: Producto)

    /**
     * Inserta una lista de items en la tabla "productos", y si algún item existe,
     * simplemente lo ignora.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarProductos(productos: List<Producto>)

    /**
     * Elimina un producto del catálogo.
     */
    @Delete
    suspend fun eliminar(producto: Producto)

    /**
     * Entrega la cantidad de items que hay en la tabla "productos"
     */
    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contarProductos(): Int
}
