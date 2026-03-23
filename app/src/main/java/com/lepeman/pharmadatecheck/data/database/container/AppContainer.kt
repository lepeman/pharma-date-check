package com.lepeman.pharmadatecheck.data.database.container

import com.lepeman.pharmadatecheck.data.repositories.AuxiliarRepository
import com.lepeman.pharmadatecheck.data.repositories.EmpresaRepository
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRevisadoRepository
import com.lepeman.pharmadatecheck.data.repositories.SesionRevisionRepository

/**
 * Interfaz que define el contenedor de dependencias de la aplicación.
 * 
 * Actúa como un punto central para acceder a todos los repositorios de datos,
 * permitiendo una gestión organizada de las dependencias y facilitando la
 * inyección de las mismas en los ViewModels.
 */
interface AppContainer {
    /** Repositorio para la gestión de auxiliares. */
    val auxiliarRepository: AuxiliarRepository
    
    /** Repositorio para la gestión de la información de la empresa. */
    val empresaRepository: EmpresaRepository
    
    /** Repositorio para la gestión de laboratorios. */
    val laboratorioRepository: LaboratorioRepository
    
    /** Repositorio para la gestión del catálogo de productos. */
    val productoRepository: ProductoRepository
    
    /** Repositorio para la gestión de las políticas de canje. */
    val politicaCanjeRepository: PoliticaCanjeRepository
    
    /** Repositorio para la gestión de productos revisados. */
    val productoRevisadoRepository: ProductoRevisadoRepository
    
    /** Repositorio para la gestión de las sesiones de revisión. */
    val sesionRevisionRepository: SesionRevisionRepository
}
