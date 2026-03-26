package com.lepeman.pharmadatecheck.data.local

sealed class ResultadoImportacion {
    data class Exito(val importados: Int, val omitidos: Int): ResultadoImportacion()
    data class Error(val mensaje: String): ResultadoImportacion()
}
