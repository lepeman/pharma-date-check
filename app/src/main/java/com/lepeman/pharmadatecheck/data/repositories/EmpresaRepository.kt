package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio para la gestión de empresas (razones sociales).
 *
 * Define las operaciones disponibles sobre la entidad [Empresa].
 * La implementación concreta es [OfflineEmpresaRepository], que delega
 * en [EmpresaDao] para el acceso a la base de datos local Room.
 */
interface EmpresaRepository {

    /** Retorna todas las empresas registradas como flujo reactivo. */
    fun obtenerTodas(): Flow<List<Empresa>>

    /** Retorna la razón social de la empresa con el [id] indicado. */
    suspend fun obtenerNombreEmpresa(id: Int): String

    /**
     * Retorna el id de la empresa cuya razón social coincide exactamente
     * con [razonSocial], o null si no existe.
     */
    suspend fun obtenerIdPorNombre(razonSocial: String): Int?

    /**
     * Retorna como flujo reactivo las empresas cuya razón social contiene
     * [query] como subcadena. Usado por el autocompletado del formulario
     * de políticas de canje.
     */
    fun buscarItems(query: String): Flow<List<Empresa>>

    /** Actualiza los datos de una empresa existente. */
    suspend fun actualizar(empresa: Empresa)

    /** Inserta una nueva empresa. Si ya existe con el mismo id, se ignora. */
    suspend fun insertar(empresa: Empresa)

    /** Elimina una empresa de la base de datos. */
    suspend fun eliminar(empresa: Empresa)
}