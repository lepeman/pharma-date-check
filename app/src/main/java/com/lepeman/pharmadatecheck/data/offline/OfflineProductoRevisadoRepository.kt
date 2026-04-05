package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.ProductoRevisadoDao
import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import com.lepeman.pharmadatecheck.data.repositories.ProductoRevisadoRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación offline de [ProductoRevisadoRepository].
 *
 * Delega todas las operaciones directamente a [ProductoRevisadoDao], que accede
 * a la tabla "productos_revisados" de la base de datos local Room. Al ser una
 * implementación offline, no realiza llamadas a servicios remotos ni lógica
 * adicional más allá de la delegación al DAO.
 *
 * @property productoRevisadoDao DAO para acceder a la tabla "productos_revisados".
 */
class OfflineProductoRevisadoRepository(
    private val productoRevisadoDao: ProductoRevisadoDao
) : ProductoRevisadoRepository {

    /** Retorna el historial completo de productos revisados como flujo reactivo. */
    override fun obtenerTodos(): Flow<List<ProductoRevisado>> =
        productoRevisadoDao.obtenerTodos()

    /**
     * Retorna como flujo reactivo todos los productos revisados durante la sesión
     * con el [sesionId] indicado. Usado por [HistorialViewModel] para cargar el
     * detalle de una sesión seleccionada en la pantalla de historial.
     */
    override fun obtenerPorSesion(sesionId: Int): Flow<List<ProductoRevisado>> =
        productoRevisadoDao.obtenerPorSesion(sesionId)

    /** Actualiza un registro de producto revisado existente en la base de datos. */
    override suspend fun actualizar(productoRevisado: ProductoRevisado) =
        productoRevisadoDao.actualizar(productoRevisado)

    /**
     * Inserta un nuevo registro de producto revisado. Usado por [ScanViewModel]
     * al confirmar la clasificación de cada producto escaneado durante una sesión.
     */
    override suspend fun insertar(productoRevisado: ProductoRevisado) =
        productoRevisadoDao.insertar(productoRevisado)

    /** Elimina un registro de producto revisado de la base de datos. */
    override suspend fun eliminar(productoRevisado: ProductoRevisado) =
        productoRevisadoDao.eliminar(productoRevisado)
}