package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.SesionRevisionDao
import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision
import com.lepeman.pharmadatecheck.data.repositories.SesionRevisionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Implementación offline del repositorio para las sesiones de revisión.
 * Utiliza [SesionRevisionDao] como fuente de datos persistente.
 *
 * @property sesionRevisionDao El DAO para acceder a la tabla de sesiones de revisión.
 */
class OfflineSesionRevisionRepository(private val sesionRevisionDao: SesionRevisionDao) : SesionRevisionRepository {
    
    /**
     * Obtiene un flujo con todas las sesiones de revisión registradas.
     */
    override fun obtenerTodas(): Flow<List<SesionRevision>> = sesionRevisionDao.obtenerTodas()

    /**
     * Se obtiene una sesión por medio del Id
     */
    override suspend fun buscarPorId(id: Int): SesionRevision? = sesionRevisionDao.buscarPorId(id)

    /**
     * Actualiza la información de una sesión existente (ej. al finalizarla).
     */
    override suspend fun actualizar(sesionRevision: SesionRevision) = sesionRevisionDao.actualizar(sesionRevision)

    /**
     * Inserta una nueva sesión de revisión en la base de datos.
     */
    override suspend fun insertar(sesionRevision: SesionRevision): Long = sesionRevisionDao.insertar(sesionRevision)

    /**
     * Elimina una sesión de revisión y sus datos asociados.
     */
    override suspend fun eliminar(sesionRevision: SesionRevision) = sesionRevisionDao.eliminar(sesionRevision)

    /**
     * Inicia sesión el auxiliar
     */
    override suspend fun iniciarSesion(auxiliarId: Int): Int {
        val sesion = SesionRevision(
            auxiliarId = auxiliarId,
            fechaInicio = LocalDateTime.now()
        )
        return sesionRevisionDao.insertar(sesion).toInt()
    }

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
