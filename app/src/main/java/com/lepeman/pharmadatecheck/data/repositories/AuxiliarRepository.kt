package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.Auxiliar
import kotlinx.coroutines.flow.Flow

interface AuxiliarRepository {

    fun obtenerTodosStream(): Flow<List<Auxiliar>>

    suspend fun obtenerAuxiliarPorId(id: Int): Auxiliar?

    suspend fun obtenerAuxiliarPorRut(rut: String): Auxiliar?

    suspend fun actualizarStream(auxiliar: Auxiliar)

    suspend fun insertar(auxiliar: Auxiliar)

    suspend fun eliminar(auxiliar: Auxiliar)

}