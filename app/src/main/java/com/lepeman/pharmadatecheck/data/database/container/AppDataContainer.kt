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
 * Implementación del contenedor de dependencias [AppContainer] para la aplicación.
 *
 * Esta clase se encarga de instanciar y proveer los repositorios necesarios para el manejo de datos,
 * utilizando implementaciones "Offline" que interactúan directamente con la base de datos local Room.
 * Se utiliza el patrón "Lazy loading" para asegurar que los repositorios solo se instancien cuando sea necesario.
 *
 * @property context El contexto de la aplicación necesario para inicializar la base de datos.
 */
class AppDataContainer(private val context: Context): AppContainer {

    /**
     * Repositorio para la gestión de auxiliares de farmacia.
     */
    override val auxiliarRepository: AuxiliarRepository by lazy {
        OfflineAuxiliarRepository(AppDatabase.getDatabase(context).auxiliarDao())
    }

    /**
     * Repositorio para la gestión de la información de la empresa.
     */
    override val empresaRepository: EmpresaRepository by lazy {
        OfflineEmpresaRepository(AppDatabase.getDatabase(context).empresaDao())
    }

    /**
     * Repositorio para la gestión de laboratorios farmacéuticos.
     */
    override val laboratorioRepository: LaboratorioRepository by lazy {
        OfflineLaboratorioRepository(AppDatabase.getDatabase(context).laboratorioDao())
    }

    /**
     * Repositorio para la gestión del catálogo de productos.
     */
    override val productoRepository: ProductoRepository by lazy {
        OfflineProductoRepository(AppDatabase.getDatabase(context).productoDao())
    }

    /**
     * Repositorio para la gestión de las políticas de canje por laboratorio.
     */
    override val politicaCanjeRepository: PoliticaCanjeRepository by lazy {
        OfflinePoliticaCanjeRepository(AppDatabase.getDatabase(context).politicaCanjeDao())
    }

    /**
     * Repositorio para la gestión de los productos que ya han sido revisados.
     */
    override val productoRevisado: ProductoRevisadoRepository by lazy {
        OfflineProductoRevisadoRepository(AppDatabase.getDatabase(context).productoRevisadoDao())
    }

    /**
     * Repositorio para la gestión de las sesiones de revisión de inventario.
     */
    override val sesionRepository: SesionRevisionRepository by lazy {
        OfflineSesionRevisionRepository(AppDatabase.getDatabase(context).sesionRevisionDao())
    }

}
