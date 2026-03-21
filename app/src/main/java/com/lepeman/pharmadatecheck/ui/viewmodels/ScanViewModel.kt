package com.lepeman.pharmadatecheck.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.lepeman.pharmadatecheck.data.repositories.AuxiliarRepository
import com.lepeman.pharmadatecheck.data.repositories.EmpresaRepository
import com.lepeman.pharmadatecheck.data.repositories.LaboratorioRepository
import com.lepeman.pharmadatecheck.data.repositories.PoliticaCanjeRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRepository
import com.lepeman.pharmadatecheck.data.repositories.ProductoRevisadoRepository
import com.lepeman.pharmadatecheck.data.repositories.SesionRevisionRepository

class ScanViewModel(
    private val auxiliarRepository: AuxiliarRepository,
    private val empresaRepository: EmpresaRepository,
    private val laboratorioRepository: LaboratorioRepository,
    private val productoRepository: ProductoRepository,
    private val politicaCanjeRepository: PoliticaCanjeRepository,
    private val productoRevisadoRepository: ProductoRevisadoRepository,
    private val sesionRevisionRepository: SesionRevisionRepository
) : ViewModel() {

    sealed class ScanUiState {
        object Idle: ScanUiState()
        object Cargando: ScanUiState()
        object ProductoNoEncontrado: ScanUiState()
        data class Resultado(
            val resultado: ResultadoClasificacion
        ) : ScanUiState()
        data class Error(val mensaje: String): ScanUiState()
    }

}