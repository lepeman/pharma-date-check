package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.Producto
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio para la gestión del catálogo de productos farmacéuticos.
 *
 * Define las operaciones disponibles sobre la entidad [Producto].
 * La implementación concreta es [OfflineProductoRepository], que delega
 * en [ProductoDao] para el acceso a la base de datos local Room.
 */
interface ProductoRepository {

    /** Retorna el catálogo completo de productos como flujo reactivo. */
    fun obtenerTodos(): Flow<List<Producto>>

    /**
     * Retorna el producto cuyo código EAN-13 coincide con [EAN13], o null
     * si no existe en el catálogo. Usado por [ScanViewModel] para identificar
     * el producto escaneado antes de clasificarlo.
     */
    suspend fun buscarPorEAN13(EAN13: String): Producto?

    /** Actualiza los datos de un producto existente en la base de datos. */
    suspend fun actualizar(producto: Producto)

    /** Inserta un producto. Si ya existe con el mismo codigoEAN13, se ignora. */
    suspend fun insertar(producto: Producto)

    /** Elimina un producto del catálogo. */
    suspend fun eliminar(producto: Producto)

    /**
     * Retorna la cantidad total de productos registrados en la tabla.
     * Usado para calcular el número de registros efectivamente importados
     * tras una operación de carga CSV.
     */
    suspend fun contarProductos(): Int

    /**
     * Inserta una lista de productos de forma masiva. Usado en la importación
     * desde CSV en la pantalla de configuración. Los registros duplicados
     * se ignoran.
     */
    suspend fun insertarProductos(productos: List<Producto>)
}