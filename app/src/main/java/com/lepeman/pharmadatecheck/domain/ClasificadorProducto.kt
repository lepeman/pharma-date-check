package com.lepeman.pharmadatecheck.domain

import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import java.time.LocalDate

data class ResultadoClasificacion(
    val producto: Producto,
    val fechaVencimiento: LocalDate,
    val clasificacion: Clasificacion,
    val diasRestantes: Int,
    val periodoRestante: String,
    val diasAnticipacionCanje: Int,
    val fechaLimiteCanje: LocalDate?,
    val nombreLaboratorio: String
)

enum class Clasificacion {
    VIGENTE,
    CANJEABLE,
    VENCIDO
}

class ClasificadorProducto {
    fun clasificar(
        producto: Producto,
        fechaVencimiento: LocalDate,
        politica: PoliticaCanje?,
        nombreLaboratorio: String,
        fechaActual: LocalDate = LocalDate.now()
    ): ResultadoClasificacion {
        val diasRestantes = fechaActual.until(fechaVencimiento).days
        val periodoRestante = "${diasRestantes} días"

        if (politica == null) {
            val clasificacion = if (fechaActual >= fechaVencimiento) {
                Clasificacion.VENCIDO
            } else {
                Clasificacion.VIGENTE
            }
            return ResultadoClasificacion(
                producto = producto,
                fechaVencimiento = fechaVencimiento,
                clasificacion = clasificacion,
                diasRestantes = diasRestantes,
                periodoRestante = periodoRestante,
                diasAnticipacionCanje = 0,
                fechaLimiteCanje = null,
                nombreLaboratorio = nombreLaboratorio
            )
        }

        val fechaLimiteCanje = fechaVencimiento.minusDays(145)

        val clasificacion = when {
            fechaActual >= fechaVencimiento -> Clasificacion.VENCIDO
            fechaActual >= fechaLimiteCanje -> Clasificacion.CANJEABLE
            else                            -> Clasificacion.VIGENTE
        }

        return ResultadoClasificacion(
            producto = producto,
            fechaVencimiento = fechaVencimiento,
            clasificacion = clasificacion,
            diasRestantes = diasRestantes,
            periodoRestante = periodoRestante,
            diasAnticipacionCanje = 56,
            fechaLimiteCanje = fechaLimiteCanje,
            nombreLaboratorio = nombreLaboratorio
        )
    }
}