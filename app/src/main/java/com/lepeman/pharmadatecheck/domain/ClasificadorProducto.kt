package com.lepeman.pharmadatecheck.domain

import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import java.time.LocalDate
import java.time.YearMonth

data class ResultadoClasificacion(
    val producto: Producto,
    val nombreLaboratorio: String,
    val fechaVencimiento: LocalDate,
    val clasificacion: Clasificacion,
    val diasRestantes: Long,
    val fechaLimiteCanje: LocalDate?
)

enum class Clasificacion {
    VIGENTE,
    CANJEABLE,
    VENCIDO
}

object ClasificadorProducto {
    fun clasificar(
        producto: Producto,
        nombreLaboratorio: String,
        fechaVencimiento: LocalDate,
        politica: PoliticaCanje?,
        fechaActual: LocalDate = LocalDate.now()
    ): ResultadoClasificacion {

        val diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(fechaActual, fechaVencimiento)

        if (politica == null) {
            val clasificacion = if (fechaActual >= fechaVencimiento) {
                Clasificacion.VENCIDO
            } else {
                Clasificacion.VIGENTE
            }
            return ResultadoClasificacion(
                producto = producto,
                nombreLaboratorio = nombreLaboratorio,
                fechaVencimiento = fechaVencimiento,
                clasificacion = clasificacion,
                diasRestantes = diasRestantes,
                fechaLimiteCanje = null
            )
        }

        // Producto no sujeto a vencimiento
        if (!politica.vencimiento) {
            return ResultadoClasificacion(
                producto = producto,
                nombreLaboratorio = nombreLaboratorio,
                fechaVencimiento = fechaVencimiento,
                clasificacion = Clasificacion.VIGENTE,
                diasRestantes = diasRestantes,
                fechaLimiteCanje = null
            )
        }

        // Producto vencido — pasa a merma
        if (fechaActual >= fechaVencimiento) {
            return ResultadoClasificacion(
                producto = producto,
                nombreLaboratorio = nombreLaboratorio,
                fechaVencimiento = fechaVencimiento,
                clasificacion = Clasificacion.VENCIDO,
                diasRestantes = diasRestantes,
                fechaLimiteCanje = null)
        }

        // Verificar si la fecha de vencimiento cae en algún mes de la política
        val periodoVencimiento = YearMonth.from(fechaVencimiento)
        val mesesPolitica = listOfNotNull(
            politica.mesUno?.let { YearMonth.from(it) },
            politica.mesDos?.let { YearMonth.from(it) },
            politica.mesTres?.let { YearMonth.from(it) }
        )

        val fechaLimiteCanje = if (periodoVencimiento in mesesPolitica)
            fechaVencimiento.withDayOfMonth(1) else null

        val clasificacion = if (periodoVencimiento in mesesPolitica)
            Clasificacion.CANJEABLE
        else
            Clasificacion.VIGENTE

        return ResultadoClasificacion(
            producto = producto,
            nombreLaboratorio = nombreLaboratorio,
            fechaVencimiento = fechaVencimiento,
            clasificacion = clasificacion,
            diasRestantes = diasRestantes,
            fechaLimiteCanje = fechaLimiteCanje
        )
    }
}