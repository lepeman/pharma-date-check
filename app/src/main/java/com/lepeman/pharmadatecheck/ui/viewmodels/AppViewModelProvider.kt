package com.lepeman.pharmadatecheck.ui.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lepeman.pharmadatecheck.DateCheckApplication

/**
 * Proveedor centralizado de fábricas de ViewModels para la aplicación.
 *
 * Implementa el patrón de inyección de dependencias manual recomendado por
 * Google para proyectos Android sin Hilt ni Dagger. Cada [initializer] define
 * cómo construir un ViewModel específico, inyectando los repositorios
 * necesarios desde el contenedor de dependencias [DateCheckApplication.container].
 *
 * Los repositorios se acceden mediante la extensión [pharmaApplication], que
 * recupera la instancia de [DateCheckApplication] desde las [CreationExtras]
 * del sistema, garantizando que los ViewModels no instancien sus dependencias
 * directamente ni tengan conocimiento del mecanismo de almacenamiento subyacente.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Fábrica de ScanViewModel
        initializer {
            ScanViewModel(
                auxiliarRepository       = pharmaApplication().container.auxiliarRepository,
                laboratorioRepository    = pharmaApplication().container.laboratorioRepository,
                productoRepository       = pharmaApplication().container.productoRepository,
                politicaCanjeRepository  = pharmaApplication().container.politicaCanjeRepository,
                sesionRevisionRepository = pharmaApplication().container.sesionRevisionRepository,
                productoRevisadoRepository = pharmaApplication().container.productoRevisadoRepository
            )
        }

        // Fábrica de CanjeViewModel
        initializer {
            CanjeViewModel(
                politicaCanjeRepository = pharmaApplication().container.politicaCanjeRepository,
                laboratorioRepository   = pharmaApplication().container.laboratorioRepository,
                empresaRepository       = pharmaApplication().container.empresaRepository
            )
        }

        // Fábrica de HistorialViewModel
        initializer {
            HistorialViewModel(
                auxiliarRepository         = pharmaApplication().container.auxiliarRepository,
                sesionRevisionRepository   = pharmaApplication().container.sesionRevisionRepository,
                productoRevisadoRepository = pharmaApplication().container.productoRevisadoRepository
            )
        }

        // Fábrica de ConfigViewModel
        initializer {
            ConfigViewModel(
                productoRepository    = pharmaApplication().container.productoRepository,
                laboratorioRepository = pharmaApplication().container.laboratorioRepository
            )
        }
    }
}

/**
 * Extensión que recupera la instancia de [DateCheckApplication] desde las
 * [CreationExtras] del sistema durante la construcción de un ViewModel.
 *
 * Permite acceder al contenedor de dependencias de la aplicación sin
 * requerir que los ViewModels extiendan [AndroidViewModel] ni tengan
 * referencia directa al contexto de Android.
 */
fun CreationExtras.pharmaApplication(): DateCheckApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DateCheckApplication