package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import kotlinx.coroutines.flow.Flow

interface PoliticaCanjeRepository {

    fun obtenerTodas(): Flow<List<PoliticaCanje>>

    suspend fun obtenerPoliticaPorLaboratorio(laboratorioId: Int): PoliticaCanje

    suspend fun actualizar(politicaCanje: PoliticaCanje)

    suspend fun insertar(politicaCanje: PoliticaCanje)

    suspend fun prepoblarSiVacio()

    suspend fun eliminar(politicaCanje: PoliticaCanje)

    suspend fun eliminarPorId(id: Int)

}