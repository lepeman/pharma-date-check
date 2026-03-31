package com.lepeman.pharmadatecheck.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de acceso a datos (DAO) para el registro de productos revisados [ProductoRevisado].
 */
@Dao
interface ProductoRevisadoDao {
    /**
     * Obtiene el historial de todos los productos que han sido revisados.
     */
    @Query("SELECT * FROM productos_revisados")
    fun obtenerTodos(): Flow<List<ProductoRevisado>>

    /**
     * Obtiene todos los productos revisados de una sesión específica
     */
    @Query("SELECT * FROM productos_revisados WHERE sesionId = :sesionId")
    fun obtenerPorSesion(sesionId: Int): Flow<List<ProductoRevisado>>
    
    /**
     * Actualiza un registro de revisión de producto.
     */
    @Update
    suspend fun actualizar(productoRevisado: ProductoRevisado)
    
    /**
     * Inserta un nuevo registro de revisión de producto.
     */
    @Insert
    suspend fun insertar(productoRevisado: ProductoRevisado)
    
    /**
     * Elimina un registro de revisión de producto.
     */
    @Delete
    suspend fun eliminar(productoRevisado: ProductoRevisado)
}
