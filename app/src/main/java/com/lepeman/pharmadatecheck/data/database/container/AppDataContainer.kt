package com.lepeman.pharmadatecheck.data.database.container

import android.content.Context
import com.lepeman.pharmadatecheck.data.database.AppDatabase
import com.lepeman.pharmadatecheck.data.offline.OfflineAuxiliarRepository
import com.lepeman.pharmadatecheck.data.offline.OfflineEmpresaRepository
import com.lepeman.pharmadatecheck.data.offline.OfflineLaboratorioRepository
import com.lepeman.pharmadatecheck.data.offline.OfflinePoliticaCanjeRepository
import com.lepeman.pharmadatecheck.data.offline.OfflineProductoRepository
import com.lepeman.pharmadatecheck.data.offline.OfflineProductoRevisadoRepository
import com.lepeman.pharmadatecheck.data.offline.OfflineSesionRevisionRepository
import com.lepeman.pharmadatecheck.data.repositories.AuxiliarRepository
import com.lepeman.pharmadatecheck.data.repositories.EmpresaRepository
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRevisadoRepository
import com.lepeman.pharmadatecheck.data.repositories.SesionRevisionRepository

/**
 * Implementación concreta de [AppContainer].
 *
 * Instancia cada repositorio mediante su implementación offline correspondiente,
 * conectándola al DAO provisto por [AppDatabase]. La inicialización es diferida
 * (lazy), por lo que cada repositorio se crea únicamente la primera vez que es
 * accedido, reduciendo el tiempo de arranque de la aplicación.
 *
 * @param context Contexto de la aplicación, requerido por [AppDatabase] para
 * localizar o crear el archivo de base de datos en el almacenamiento del dispositivo.
 */
class AppDataContainer(private val context: Context) : AppContainer {

    /** Acceso a auxiliares de farmacia registrados en el sistema. */
    override val auxiliarRepository: AuxiliarRepository by lazy {
        OfflineAuxiliarRepository(AppDatabase.getDatabase(context).auxiliarDao())
    }

    /** Acceso a razones sociales (empresas) asociadas a los laboratorios. */
    override val empresaRepository: EmpresaRepository by lazy {
        OfflineEmpresaRepository(AppDatabase.getDatabase(context).empresaDao())
    }

    /** Acceso al catálogo de laboratorios farmacéuticos. */
    override val laboratorioRepository: LaboratorioRepository by lazy {
        OfflineLaboratorioRepository(AppDatabase.getDatabase(context).laboratorioDao())
    }

    /** Acceso al catálogo de productos farmacéuticos. */
    override val productoRepository: ProductoRepository by lazy {
        OfflineProductoRepository(AppDatabase.getDatabase(context).productoDao())
    }

    /** Acceso a las políticas de canje configuradas por laboratorio. */
    override val politicaCanjeRepository: PoliticaCanjeRepository by lazy {
        OfflinePoliticaCanjeRepository(AppDatabase.getDatabase(context).politicaCanjeDao())
    }

    /** Acceso al registro de productos revisados agrupados por sesión. */
    override val productoRevisadoRepository: ProductoRevisadoRepository by lazy {
        OfflineProductoRevisadoRepository(AppDatabase.getDatabase(context).productoRevisadoDao())
    }

    /** Acceso a las sesiones de revisión de inventario. */
    override val sesionRevisionRepository: SesionRevisionRepository by lazy {
        OfflineSesionRevisionRepository(AppDatabase.getDatabase(context).sesionRevisionDao())
    }
}