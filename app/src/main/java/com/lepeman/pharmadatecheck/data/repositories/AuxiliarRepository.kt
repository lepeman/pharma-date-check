package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.Auxiliar
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio para la gestión de auxiliares de farmacia.
 *
 * Define las operaciones disponibles sobre la entidad [Auxiliar].
 * La implementación concreta es [OfflineAuxiliarRepository], que delega
 * en [AuxiliarDao] para el acceso a la base de datos local Room.
 */
interface AuxiliarRepository {

    /** Retorna todos los auxiliares registrados como flujo reactivo. */
    fun obtenerTodos(): Flow<List<Auxiliar>>

    /** Retorna el auxiliar con el [id] indicado, o null si no existe. */
    suspend fun obtenerAuxiliarPorId(id: Int): Auxiliar?

    /** Retorna el auxiliar cuyo RUT coincide con [rut], o null si no existe. */
    suspend fun obtenerAuxiliarPorRut(rut: String): Auxiliar?

    /** Retorna el nombre completo del auxiliar con el [id] indicado, o null si no existe. */
    suspend fun obtenerNombrePorId(id: Int): String?

    /** Actualiza los datos de un auxiliar existente. */
    suspend fun actualizar(auxiliar: Auxiliar)

    /** Inserta un nuevo auxiliar. Si ya existe con el mismo id, se ignora. */
    suspend fun insertar(auxiliar: Auxiliar)

    /** Elimina un auxiliar de la base de datos. */
    suspend fun eliminar(auxiliar: Auxiliar)
}