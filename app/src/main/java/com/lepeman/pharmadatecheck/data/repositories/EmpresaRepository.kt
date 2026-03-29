package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import kotlinx.coroutines.flow.Flow

interface EmpresaRepository {

    fun obtenerTodas(): Flow<List<Empresa>>

    fun obtenerNombreEmpresa(id: Int): String

    fun obtenerIdPorNombre(razonSocial: String): Int

    fun buscarItems(query: String): Flow<List<Empresa>>

    suspend fun actualizar(empresa: Empresa)

    suspend fun insertar(empresa: Empresa)

    suspend fun eliminar(empresa: Empresa)

}