package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import kotlinx.coroutines.flow.Flow

interface PoliticaCanjeRepository {

    fun obtenerTodas(): Flow<List<PoliticaCanje>>

    suspend fun actualizar(politicaCanje: PoliticaCanje)

    suspend fun insertar(politicaCanje: PoliticaCanje)

    suspend fun eliminar(politicaCanje: PoliticaCanje)

}