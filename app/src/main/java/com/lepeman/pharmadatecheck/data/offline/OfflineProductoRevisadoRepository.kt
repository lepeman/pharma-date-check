package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.ProductoRevisadoDao
import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import com.lepeman.pharmadatecheck.data.repositories.ProductoRevisadoRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline del repositorio para la gestión de productos revisados.
 * Utiliza [ProductoRevisadoDao] como fuente de datos persistente local.
 *
 * @property productoRevisadoDao El DAO para acceder a la tabla de productos revisados.
 */
class OfflineProductoRevisadoRepository(private val productoRevisadoDao: ProductoRevisadoDao) : ProductoRevisadoRepository {
    
    /**
     * Obtiene un flujo de datos con la lista de todos los productos revisados.
     */
    override fun obtenerTodos(): Flow<List<ProductoRevisado>> = productoRevisadoDao.obtenerTodos()

    /**
     * Obtiene un flujo de datos con la lista de productos revisados asociados a una sesión específica.
     */
    override fun obtenerPorSesion(sesionId: Int): Flow<List<ProductoRevisado>> = productoRevisadoDao.obtenerPorSesion(sesionId)

    /**
     * Actualiza la información de un registro de producto revisado en la base de datos local.
     */
    override suspend fun actualizar(productoRevisado: ProductoRevisado) = productoRevisadoDao.actualizar(productoRevisado)

    /**
     * Inserta un nuevo registro de producto revisado en la base de datos local.
     */
    override suspend fun insertar(productoRevisado: ProductoRevisado) = productoRevisadoDao.insertar(productoRevisado)

    /**
     * Elimina un registro de producto revisado de la base de datos local.
     */
    override suspend fun eliminar(productoRevisado: ProductoRevisado) = productoRevisadoDao.eliminar(productoRevisado)
}
