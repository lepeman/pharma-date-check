package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.ProductoDao
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import com.lepeman.pharmadatecheck.data.repositories.ProductoRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline del repositorio para la gestión de productos.
 * Utiliza [ProductoDao] como fuente de datos persistente local.
 *
 * @property productoDao El DAO para acceder a la tabla de productos.
 */
class OfflineProductoRepository(private val productoDao: ProductoDao) : ProductoRepository {
    
    /**
     * Obtiene un flujo de datos con la lista de todos los productos.
     */
    override fun obtenerTodos(): Flow<List<Producto>> = productoDao.obtenerTodos()

    /**
     * Actualiza la información de un producto en la base de datos local.
     */
    override suspend fun actualizar(producto: Producto) = productoDao.actualizar(producto)

    /**
     * Inserta un nuevo producto en la base de datos local.
     */
    override suspend fun insertar(producto: Producto) = productoDao.insertar(producto)

    /**
     * Elimina un producto de la base de datos local.
     */
    override suspend fun eliminar(producto: Producto) = productoDao.eliminar(producto)
}
