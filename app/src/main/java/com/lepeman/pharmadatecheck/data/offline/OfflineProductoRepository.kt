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
     * Se obtiene un producto por medio del código EAN13
     */
    override suspend fun buscarPorEAN13(EAN13: String): Producto? = productoDao.buscarPorEAN13(EAN13)

    /**
     * Actualiza la información de un producto en la base de datos local.
     */
    override suspend fun actualizar(producto: Producto) = productoDao.actualizar(producto)

    /**
     * Inserta un nuevo producto en la base de datos local.
     */
    override suspend fun insertar(producto: Producto) = productoDao.insertar(producto)

    /**
     * Inserta una lista de items en la tabla "productos", y si algún item existe,
     * simplemente lo ignora.
     */
    override suspend fun insertarProductos(productos: List<Producto>) = productoDao.insertarProductos(productos)

    /**
     * Elimina un producto de la base de datos local.
     */
    override suspend fun eliminar(producto: Producto) = productoDao.eliminar(producto)

    /**
     * Cuenta la cantidad de items estan contenidos en la tabla
     */
    override suspend fun contarProductos(): Int = productoDao.contarProductos()
}
