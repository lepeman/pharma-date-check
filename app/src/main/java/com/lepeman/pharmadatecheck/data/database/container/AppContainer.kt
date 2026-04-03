package com.lepeman.pharmadatecheck.data.database.container

import com.lepeman.pharmadatecheck.data.repositories.AuxiliarRepository
import com.lepeman.pharmadatecheck.data.repositories.EmpresaRepository
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRevisadoRepository
import com.lepeman.pharmadatecheck.data.repositories.SesionRevisionRepository

/**
 * Contrato del contenedor de dependencias de la aplicación.
 *
 * Define el punto de acceso centralizado a todos los repositorios de datos.
 * La implementación concreta [AppDataContainer] instancia cada repositorio
 * con inicialización diferida (lazy), de modo que solo se crean cuando son
 * requeridos por primera vez.
 *
 * Los ViewModels acceden a estas dependencias a través de [AppViewModelProvider],
 * sin instanciar repositorios directamente.
 */
interface AppContainer {

    /** Repositorio para la gestión de auxiliares de farmacia. */
    val auxiliarRepository: AuxiliarRepository

    /** Repositorio para la gestión de razones sociales (empresas). */
    val empresaRepository: EmpresaRepository

    /** Repositorio para la gestión del catálogo de laboratorios. */
    val laboratorioRepository: LaboratorioRepository

    /** Repositorio para la gestión del catálogo de productos farmacéuticos. */
    val productoRepository: ProductoRepository

    /** Repositorio para la gestión de políticas de canje por laboratorio. */
    val politicaCanjeRepository: PoliticaCanjeRepository

    /** Repositorio para el registro de productos revisados por sesión. */
    val productoRevisadoRepository: ProductoRevisadoRepository

    /** Repositorio para la gestión de sesiones de revisión de inventario. */
    val sesionRevisionRepository: SesionRevisionRepository
}