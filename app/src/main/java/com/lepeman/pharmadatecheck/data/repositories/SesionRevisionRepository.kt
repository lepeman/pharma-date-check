package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import kotlinx.coroutines.flow.Flow

interface SesionRevisionRepository {

    fun obtenerTodas(): Flow<List<SesionRevision>>

    suspend fun actualizar(sesionRevision: SesionRevision)

    suspend fun insertar(sesionRevision: SesionRevision)

    suspend fun eliminar(sesionRevision: SesionRevision)

}