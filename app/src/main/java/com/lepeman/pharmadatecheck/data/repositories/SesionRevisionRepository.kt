package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import kotlinx.coroutines.flow.Flow

interface SesionRevisionRepository {

    fun obtenerTodas(): Flow<List<SesionRevision>>

    suspend fun buscarPorId(id: Int): SesionRevision?

    suspend fun actualizar(sesionRevision: SesionRevision)

    suspend fun insertar(sesionRevision: SesionRevision): Long

    suspend fun eliminar(sesionRevision: SesionRevision)

    suspend fun iniciarSesion(auxiliarId: Int): Int

    suspend fun cerrarSesion(id: Int, vigentes: Int, canjeables: Int, vencidos: Int)
}