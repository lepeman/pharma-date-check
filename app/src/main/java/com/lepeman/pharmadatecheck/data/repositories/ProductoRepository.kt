package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {

    fun obtenerTodos(): Flow<List<Producto>>

    suspend fun buscarPorEAN13(EAN13: String): Producto?

    suspend fun actualizar(producto: Producto)

    suspend fun insertar(producto: Producto)

    suspend fun eliminar(producto: Producto)

    suspend fun contarProductos(): Int

    suspend fun insertarProductos(productos: List<Producto>)

}