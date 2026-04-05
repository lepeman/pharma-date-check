package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.ProductoDao
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import com.lepeman.pharmadatecheck.data.repositories.ProductoRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline de [ProductoRepository].
 *
 * Delega todas las operaciones directamente a [ProductoDao], que accede
 * a la tabla "productos" de la base de datos local Room. Al ser una
 * implementación offline, no realiza llamadas a servicios remotos ni
 * lógica adicional más allá de la delegación al DAO.
 *
 * @property productoDao DAO para acceder a la tabla "productos".
 */
class OfflineProductoRepository(private val productoDao: ProductoDao) : ProductoRepository {

    /** Retorna el catálogo completo de productos como flujo reactivo. */
    override fun obtenerTodos(): Flow<List<Producto>> =
        productoDao.obtenerTodos()

    /**
     * Retorna el producto cuyo código EAN-13 coincide con [EAN13], o null
     * si no existe en el catálogo. Usado por [ScanViewModel] para identificar
     * el producto escaneado antes de clasificarlo.
     */
    override suspend fun buscarPorEAN13(EAN13: String): Producto? =
        productoDao.buscarPorEAN13(EAN13)

    /** Actualiza los datos de un producto existente en la base de datos. */
    override suspend fun actualizar(producto: Producto) =
        productoDao.actualizar(producto)

    /** Inserta un producto. Si ya existe con el mismo codigoEAN13, se ignora. */
    override suspend fun insertar(producto: Producto) =
        productoDao.insertar(producto)

    /**
     * Inserta una lista de productos de forma masiva. Usado en la importación
     * desde CSV en la pantalla de configuración. Los registros duplicados
     * se ignoran.
     */
    override suspend fun insertarProductos(productos: List<Producto>) =
        productoDao.insertarProductos(productos)

    /** Elimina un producto del catálogo. */
    override suspend fun eliminar(producto: Producto) =
        productoDao.eliminar(producto)

    /**
     * Retorna la cantidad total de productos registrados en la tabla.
     * Usado por [ConfigViewModel] para calcular el número de registros
     * efectivamente importados tras una operación de carga CSV.
     */
    override suspend fun contarProductos(): Int =
        productoDao.contarProductos()
}