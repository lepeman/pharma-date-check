package com.lepeman.pharmadatecheck.data.repositories

import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio para la gestión de sesiones de revisión de inventario.
 *
 * Define las operaciones disponibles sobre la entidad [SesionRevision].
 * La implementación concreta es [OfflineSesionRevisionRepository], que delega
 * en [SesionRevisionDao] para el acceso a la base de datos local Room e
 * implementa [iniciarSesion] y [cerrarSesion] como operaciones de alto nivel
 * que encapsulan el ciclo de vida de una sesión.
 */
interface SesionRevisionRepository {

    /** Retorna todas las sesiones de revisión registradas como flujo reactivo. */
    fun obtenerTodas(): Flow<List<SesionRevision>>

    /** Retorna la sesión con el [id] indicado, o null si no existe. */
    suspend fun buscarPorId(id: Int): SesionRevision?

    /** Actualiza los datos de una sesión existente en la base de datos. */
    suspend fun actualizar(sesionRevision: SesionRevision)

    /**
     * Inserta una nueva sesión de revisión y retorna su id autoGenerado.
     * El id es utilizado por [ScanViewModel] para asociar los productos
     * revisados a la sesión activa.
     */
    suspend fun insertar(sesionRevision: SesionRevision): Long

    /**
     * Elimina una sesión de revisión. Dado que [ProductoRevisado] tiene
     * relación CASCADE con [SesionRevision], esta operación elimina también
     * todos los productos revisados asociados a la sesión.
     */
    suspend fun eliminar(sesionRevision: SesionRevision)

    /**
     * Crea e inserta una nueva sesión de revisión asociada al auxiliar
     * con el [auxiliarId] indicado, registrando la fecha y hora de inicio.
     *
     * @param auxiliarId Identificador del auxiliar autenticado.
     * @return Id autoGenerado de la sesión creada.
     */
    suspend fun iniciarSesion(auxiliarId: Int): Int

    /**
     * Cierra la sesión con el [id] indicado registrando la fecha y hora de
     * término y los totales acumulados de productos clasificados por categoría.
     * Si la sesión no existe, la operación no tiene efecto.
     *
     * @param id Identificador de la sesión a cerrar.
     * @param vigentes Total de productos clasificados como VIGENTE.
     * @param canjeables Total de productos clasificados como CANJEABLE.
     * @param vencidos Total de productos clasificados como VENCIDO.
     */
    suspend fun cerrarSesion(id: Int, vigentes: Int, canjeables: Int, vencidos: Int)
}