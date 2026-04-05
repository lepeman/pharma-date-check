package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.SesionRevisionDao
import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import com.lepeman.pharmadatecheck.data.repositories.SesionRevisionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Implementación offline de [SesionRevisionRepository].
 *
 * Delega las operaciones CRUD directamente a [SesionRevisionDao]. Adicionalmente,
 * implementa [iniciarSesion] y [cerrarSesion] como operaciones de alto nivel que
 * encapsulan la lógica de ciclo de vida de una sesión de revisión, evitando que
 * los ViewModels manipulen directamente los campos de [SesionRevision].
 *
 * @property sesionRevisionDao DAO para acceder a la tabla "sesiones_revision".
 */
class OfflineSesionRevisionRepository(
    private val sesionRevisionDao: SesionRevisionDao
) : SesionRevisionRepository {

    /** Retorna todas las sesiones de revisión registradas como flujo reactivo. */
    override fun obtenerTodas(): Flow<List<SesionRevision>> =
        sesionRevisionDao.obtenerTodas()

    /** Retorna la sesión con el [id] indicado, o null si no existe. */
    override suspend fun buscarPorId(id: Int): SesionRevision? =
        sesionRevisionDao.buscarPorId(id)

    /** Actualiza los datos de una sesión existente en la base de datos. */
    override suspend fun actualizar(sesionRevision: SesionRevision) =
        sesionRevisionDao.actualizar(sesionRevision)

    /**
     * Inserta una nueva sesión de revisión y retorna su id autoGenerado.
     * El id es utilizado por [ScanViewModel] para asociar los productos
     * revisados a la sesión activa.
     */
    override suspend fun insertar(sesionRevision: SesionRevision): Long =
        sesionRevisionDao.insertar(sesionRevision)

    /**
     * Elimina una sesión de revisión. Dado que [ProductoRevisado] tiene
     * relación CASCADE con [SesionRevision], esta operación elimina también
     * todos los productos revisados asociados a la sesión.
     */
    override suspend fun eliminar(sesionRevision: SesionRevision) =
        sesionRevisionDao.eliminar(sesionRevision)

    /**
     * Crea e inserta una nueva sesión de revisión asociada al auxiliar
     * con el [auxiliarId] indicado, registrando la fecha y hora de inicio.
     *
     * @param auxiliarId Identificador del auxiliar autenticado.
     * @return Id autoGenerado de la sesión creada, usado por [ScanViewModel]
     * para asociar los productos escaneados durante la sesión.
     */
    override suspend fun iniciarSesion(auxiliarId: Int): Int {
        val sesion = SesionRevision(
            auxiliarId = auxiliarId,
            fechaInicio = LocalDateTime.now()
        )
        return sesionRevisionDao.insertar(sesion).toInt()
    }

    /**
     * Cierra la sesión con el [id] indicado registrando la fecha y hora de
     * término y los totales acumulados de productos clasificados por categoría.
     *
     * Si la sesión no existe, la operación no tiene efecto.
     *
     * @param id Identificador de la sesión a cerrar.
     * @param vigentes Total de productos clasificados como VIGENTE.
     * @param canjeables Total de productos clasificados como CANJEABLE.
     * @param vencidos Total de productos clasificados como VENCIDO.
     */
    override suspend fun cerrarSesion(
        id: Int,
        vigentes: Int,
        canjeables: Int,
        vencidos: Int
    ) {
        val sesion = sesionRevisionDao.buscarPorId(id) ?: return
        sesionRevisionDao.actualizar(
            sesion.copy(
                fechaTermino = LocalDateTime.now(),
                totalVigentes = vigentes,
                totalCanjeables = canjeables,
                totalVencidos = vencidos
            )
        )
    }
}