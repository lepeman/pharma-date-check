package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import kotlinx.coroutines.flow.Flow

interface ProductoRevisadoRepository {

    fun obtenerTodos(): Flow<List<ProductoRevisado>>

    suspend fun actualizar(productoRevisado: ProductoRevisado)

    suspend fun insertar(productoRevisado: ProductoRevisado)

    suspend fun eliminar(productoRevisado: ProductoRevisado)
}