package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.PoliticaCanjeDao
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Implementación offline de [PoliticaCanjeRepository].
 *
 * Delega las operaciones CRUD directamente a [PoliticaCanjeDao]. Adicionalmente,
 * implementa [prepoblarSiVacio] para cargar un conjunto de políticas de ejemplo
 * cuando la tabla está vacía, facilitando las pruebas funcionales del sistema
 * sin necesidad de configuración manual previa.
 *
 * @property politicaCanjeDao DAO para acceder a la tabla "politicas_canje".
 */
class OfflinePoliticaCanjeRepository(private val politicaCanjeDao: PoliticaCanjeDao) : PoliticaCanjeRepository {

    /** Retorna todas las políticas de canje como flujo reactivo. */
    override fun obtenerTodas(): Flow<List<PoliticaCanje>> =
        politicaCanjeDao.obtenerTodas()

    /**
     * Retorna la política de canje asociada al laboratorio con el [laboratorioId]
     * indicado, o null si no existe. Usado por [ClasificadorProducto] para
     * determinar si un producto escaneado es elegible para canje.
     */
    override suspend fun obtenerPoliticaPorLaboratorio(laboratorioId: Int): PoliticaCanje? =
        politicaCanjeDao.obtenerPoliticaPorLaboratorio(laboratorioId)

    /** Actualiza una política de canje existente en la base de datos. */
    override suspend fun actualizar(politicaCanje: PoliticaCanje) =
        politicaCanjeDao.actualizar(politicaCanje)

    /** Inserta una nueva política de canje. Si ya existe con el mismo id, se ignora. */
    override suspend fun insertar(politicaCanje: PoliticaCanje) =
        politicaCanjeDao.insertar(politicaCanje)

    /**
     * Inserta un conjunto de políticas de canje de ejemplo si la tabla está vacía.
     *
     * Las políticas precargadas corresponden a los laboratorios del catálogo
     * inicial de la sucursal CV105 con períodos de canje representativos.
     * Este método es invocado desde [CanjeViewModel] al detectar que no hay
     * políticas configuradas, permitiendo verificar el motor de clasificación
     * sin configuración manual previa.
     *
     * Nota: los períodos de canje aquí definidos son de ejemplo y deben ser
     * actualizados manualmente conforme los laboratorios comuniquen sus
     * condiciones reales.
     */
    override suspend fun prepoblarSiVacio() {
        val politicasIniciales = listOf(
            PoliticaCanje(id = 0, empresaId = 1, laboratorioId = 1, vencimiento = true,
                mesUno = LocalDate.parse("2026-03-01"), mesDos = LocalDate.parse("2026-04-01"), mesTres = LocalDate.parse("2026-05-01")),
            PoliticaCanje(id = 0, empresaId = 1, laboratorioId = 2, vencimiento = true,
                mesUno = LocalDate.parse("2026-03-01"), mesDos = LocalDate.parse("2026-04-01"), mesTres = LocalDate.parse("2026-05-01")),
            PoliticaCanje(id = 0, empresaId = 2, laboratorioId = 3, vencimiento = true,
                mesUno = LocalDate.parse("2026-03-01"), mesDos = LocalDate.parse("2026-04-01"), mesTres = LocalDate.parse("2026-05-01")),
            PoliticaCanje(id = 0, empresaId = 3, laboratorioId = 4, vencimiento = true,
                mesUno = LocalDate.parse("2026-04-01"), mesDos = LocalDate.parse("2026-05-01"), mesTres = LocalDate.parse("2026-06-01")),
            PoliticaCanje(id = 0, empresaId = 5, laboratorioId = 5, vencimiento = true,
                mesUno = LocalDate.parse("2026-01-01"), mesDos = LocalDate.parse("2026-02-01"), mesTres = LocalDate.parse("2026-03-01")),
        )
        politicaCanjeDao.insertarTodasLasPoliticas(politicasIniciales)
    }

    /** Elimina una política de canje de la base de datos. */
    override suspend fun eliminar(politicaCanje: PoliticaCanje) =
        politicaCanjeDao.eliminar(politicaCanje)

    /**
     * Elimina la política de canje con el [id] indicado.
     * Usado desde [CanjeViewModel] al confirmar la eliminación desde la
     * pantalla de gestión de canjes.
     */
    override suspend fun eliminarPorId(id: Int) =
        politicaCanjeDao.eliminarPorId(id)
}