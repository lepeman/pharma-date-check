package com.lepeman.pharmadatecheck.domain

import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import java.time.LocalDate
import java.time.YearMonth

data class ResultadoClasificacion(
    val producto: Producto,
    val fechaVencimiento: LocalDate,
    val clasificacion: Clasificacion,
)

enum class Clasificacion {
    VIGENTE,
    CANJEABLE,
    VENCIDO
}

object ClasificadorProducto {
    fun clasificar(
        producto: Producto,
        fechaVencimiento: LocalDate,
        politica: PoliticaCanje?,
        fechaActual: LocalDate = LocalDate.now()
    ): ResultadoClasificacion {

        if (politica == null) {
            val clasificacion = if (fechaActual >= fechaVencimiento) {
                Clasificacion.VENCIDO
            } else {
                Clasificacion.VIGENTE
            }
            return ResultadoClasificacion(
                producto = producto,
                fechaVencimiento = fechaVencimiento,
                clasificacion = clasificacion
            )
        }

        // Producto no sujeto a vencimiento
        if (!politica.vencimiento) {
            return ResultadoClasificacion(producto, fechaVencimiento, Clasificacion.VIGENTE)
        }

        // Producto vencido — pasa a merma
        if (fechaActual >= fechaVencimiento) {
            return ResultadoClasificacion(producto, fechaVencimiento, Clasificacion.VENCIDO)
        }

        // Verificar si la fecha de vencimiento cae en algún mes de la política
        val periodoVencimiento = YearMonth.from(fechaVencimiento)
        val mesesPolitica = listOfNotNull(
            politica.mesUno?.let { YearMonth.from(it) },
            politica.mesDos?.let { YearMonth.from(it) },
            politica.mesTres?.let { YearMonth.from(it) }
        )

        val clasificacion = if (periodoVencimiento in mesesPolitica)
            Clasificacion.CANJEABLE
        else
            Clasificacion.VIGENTE

        return ResultadoClasificacion(producto, fechaVencimiento, clasificacion)
    }
}