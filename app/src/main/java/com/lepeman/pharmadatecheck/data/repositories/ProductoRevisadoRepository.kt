package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio para la gestión de productos revisados.
 *
 * Define las operaciones disponibles sobre la entidad [ProductoRevisado].
 * La implementación concreta es [OfflineProductoRevisadoRepository], que
 * delega en [ProductoRevisadoDao] para el acceso a la base de datos local Room.
 */
interface ProductoRevisadoRepository {

    /** Retorna el historial completo de productos revisados como flujo reactivo. */
    fun obtenerTodos(): Flow<List<ProductoRevisado>>

    /**
     * Retorna como flujo reactivo todos los productos revisados durante la sesión
     * con el [sesionId] indicado. Usado por [HistorialViewModel] para cargar el
     * detalle de una sesión seleccionada en la pantalla de historial.
     */
    fun obtenerPorSesion(sesionId: Int): Flow<List<ProductoRevisado>>

    /** Actualiza un registro de producto revisado existente en la base de datos. */
    suspend fun actualizar(productoRevisado: ProductoRevisado)

    /**
     * Inserta un nuevo registro de producto revisado. Usado por [ScanViewModel]
     * al confirmar la clasificación de cada producto escaneado durante una sesión.
     */
    suspend fun insertar(productoRevisado: ProductoRevisado)

    /** Elimina un registro de producto revisado de la base de datos. */
    suspend fun eliminar(productoRevisado: ProductoRevisado)
}