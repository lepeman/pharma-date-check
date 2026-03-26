package com.lepeman.pharmadatecheck.ui.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lepeman.pharmadatecheck.DateCheckApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ScanViewModel(
                auxiliarRepository = pharmaApplication().container.auxiliarRepository,
                empresaRepository = pharmaApplication().container.empresaRepository,
                laboratorioRepository = pharmaApplication().container.laboratorioRepository,
                productoRepository = pharmaApplication().container.productoRepository,
                politicaCanjeRepository = pharmaApplication().container.politicaCanjeRepository,
                sesionRevisionRepository = pharmaApplication().container.sesionRevisionRepository,
                productoRevisadoRepository = pharmaApplication().container.productoRevisadoRepository
            )
        }

        initializer {
            CanjeViewModel(
                politicaCanjeRepository = pharmaApplication().container.politicaCanjeRepository,
                laboratorioRepository = pharmaApplication().container.laboratorioRepository,
                empresaRepository = pharmaApplication().container.empresaRepository
            )
        }

        initializer {
            HistorialViewModel(
                auxiliarRepository = pharmaApplication().container.auxiliarRepository,
                sesionRevisionRepository = pharmaApplication().container.sesionRevisionRepository,
                productoRevisadoRepository = pharmaApplication().container.productoRevisadoRepository
            )
        }
    }
}

fun CreationExtras.pharmaApplication(): DateCheckApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DateCheckApplication)