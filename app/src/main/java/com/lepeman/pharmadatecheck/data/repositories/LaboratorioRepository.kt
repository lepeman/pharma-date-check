package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import kotlinx.coroutines.flow.Flow

interface LaboratorioRepository {

    fun obtenerTodos(): Flow<List<Laboratorio>>

    fun obtenerNombrePorId(id: Int): String

    suspend fun actualizar(laboratorio: Laboratorio)

    suspend fun insertar(laboratorio: Laboratorio)

    suspend fun eliminar(laboratorio: Laboratorio)
}