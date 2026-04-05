package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio para la gestión de políticas de canje.
 *
 * Define las operaciones disponibles sobre la entidad [PoliticaCanje].
 * La implementación concreta es [OfflinePoliticaCanjeRepository], que delega
 * en [PoliticaCanjeDao] para el acceso a la base de datos local Room.
 */
interface PoliticaCanjeRepository {

    /** Retorna todas las políticas de canje configuradas como flujo reactivo. */
    fun obtenerTodas(): Flow<List<PoliticaCanje>>

    /**
     * Retorna la política de canje asociada al laboratorio con el [laboratorioId]
     * indicado, o null si no existe. Usado por [ClasificadorProducto] para
     * determinar si un producto escaneado es elegible para canje.
     */
    suspend fun obtenerPoliticaPorLaboratorio(laboratorioId: Int): PoliticaCanje?

    /** Actualiza una política de canje existente en la base de datos. */
    suspend fun actualizar(politicaCanje: PoliticaCanje)

    /** Inserta una nueva política de canje. Si ya existe con el mismo id, se ignora. */
    suspend fun insertar(politicaCanje: PoliticaCanje)

    /**
     * Inserta un conjunto de políticas de canje de ejemplo si la tabla está vacía.
     * Invocado desde [CanjeViewModel] para facilitar las pruebas funcionales
     * sin configuración manual previa.
     */
    suspend fun prepoblarSiVacio()

    /** Elimina una política de canje de la base de datos. */
    suspend fun eliminar(politicaCanje: PoliticaCanje)

    /**
     * Elimina la política de canje con el [id] indicado.
     * Usado desde [CanjeViewModel] al confirmar la eliminación desde
     * la pantalla de gestión de canjes.
     */
    suspend fun eliminarPorId(id: Int)
}