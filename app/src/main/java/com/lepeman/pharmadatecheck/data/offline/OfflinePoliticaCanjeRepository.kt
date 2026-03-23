package com.lepeman.pharmadatecheck.data.offline

import com.lepeman.pharmadatecheck.data.local.dao.PoliticaCanjeDao
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Implementación offline del repositorio para la gestión de las políticas de canje.
 * Utiliza [PoliticaCanjeDao] como fuente de datos persistente local.
 *
 * @property politicaCanjeDao El DAO para acceder a la tabla de políticas de canje.
 */
class OfflinePoliticaCanjeRepository(private val politicaCanjeDao: PoliticaCanjeDao) : PoliticaCanjeRepository {
    
    /**
     * Obtiene un flujo de datos con la lista de todas las políticas de canje.
     */
    override fun obtenerTodas(): Flow<List<PoliticaCanje>> = politicaCanjeDao.obtenerTodas()

    /**
     * Obtiene una lista de políticas de canje filtrada por el "Id" del laboratorio.
     */
    override suspend fun obtenerPoliticaPorLaboratorio(laboratorioId: Int): PoliticaCanje =
        politicaCanjeDao.obtenerPoliticaPorLaboratorio(laboratorioId)

    /**
     * Actualiza la información de una política de canje en la base de datos local.
     */
    override suspend fun actualizar(politicaCanje: PoliticaCanje) = politicaCanjeDao.actualizar(politicaCanje)

    /**
     * Inserta una nueva política de canje en la base de datos local.
     */
    override suspend fun insertar(politicaCanje: PoliticaCanje) = politicaCanjeDao.insertar(politicaCanje)

    /**
     * Insertar políticas de canje para prepoblar tabla
     */
    override suspend fun prepoblarSiVacio() {
        val politicasIniciales = listOf(
            PoliticaCanje(id = 0, empresaId = 1, laboratorioId = 1, vencimiento = true, mesUno = LocalDate.parse("2026-03-01"), mesDos = LocalDate.parse("2026-04-01"), mesTres = LocalDate.parse("2026-05-01")),
            PoliticaCanje(id = 0, empresaId = 1, laboratorioId = 2, vencimiento = true, mesUno = LocalDate.parse("2026-03-01"), mesDos = LocalDate.parse("2026-04-01"), mesTres = LocalDate.parse("2026-05-01")),
            PoliticaCanje(id = 0, empresaId = 2, laboratorioId = 3, vencimiento = true, mesUno = LocalDate.parse("2026-03-01"), mesDos = LocalDate.parse("2026-04-01"), mesTres = LocalDate.parse("2026-05-01")),
            PoliticaCanje(id = 0, empresaId = 3, laboratorioId = 4, vencimiento = true, mesUno = LocalDate.parse("2026-04-01"), mesDos = LocalDate.parse("2026-05-01"), mesTres = LocalDate.parse("2026-06-01")),
            PoliticaCanje(id = 0, empresaId = 5, laboratorioId = 5, vencimiento = true, mesUno = LocalDate.parse("2026-01-01"), mesDos = LocalDate.parse("2026-02-01"), mesTres = LocalDate.parse("2026-03-01")),
        )

        politicaCanjeDao.insertarTodasLasPoliticas(politicasIniciales)
    }

    /**
     * Elimina una política de canje de la base de datos local.
     */
    override suspend fun eliminar(politicaCanje: PoliticaCanje) = politicaCanjeDao.eliminar(politicaCanje)

    /**
     * Elimina una política de canje de la base de datos local por medio del id.
     */
    override suspend fun eliminarPorId(id: Int) = politicaCanjeDao.eliminarPorId(id)
}
