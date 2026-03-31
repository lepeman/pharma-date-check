package com.lepeman.pharmadatecheck.data.repositories

import android.content.Context
import android.net.Uri
import com.lepeman.pharmadatecheck.data.local.ResultadoImportacion
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import kotlinx.coroutines.flow.Flow

interface LaboratorioRepository {

    fun obtenerTodos(): Flow<List<Laboratorio>>

    suspend fun obtenerNombrePorId(id: Int): String?

    fun buscarItems(query: String): Flow<List<Laboratorio>>

    suspend fun obtenerIdPorNombre(nombre: String): Int

    suspend fun actualizar(laboratorio: Laboratorio)

    suspend fun insertar(laboratorio: Laboratorio)

    suspend fun eliminar(laboratorio: Laboratorio)

    suspend fun contarLaboratorios(): Int

    suspend fun insertarLaboratoriosDesdeCSV(context: Context, uri: Uri): ResultadoImportacion
}