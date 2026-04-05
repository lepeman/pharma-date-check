package com.lepeman.pharmadatecheck.domain

import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import java.time.LocalDate
import java.time.YearMonth

/**
 * Resultado de la clasificación de un producto farmacéutico.
 *
 * Encapsula toda la información necesaria para que la interfaz de usuario
 * muestre el estado del producto al auxiliar sin necesidad de lógica adicional.
 *
 * @property producto Producto clasificado.
 * @property nombreLaboratorio Nombre del laboratorio fabricante, resuelto
 * previamente desde el catálogo de laboratorios.
 * @property fechaVencimiento Fecha de vencimiento confirmada por el operador.
 * @property clasificacion Estado asignado al producto: [Clasificacion.VIGENTE],
 * [Clasificacion.CANJEABLE] o [Clasificacion.VENCIDO].
 * @property diasRestantes Días entre la fecha actual y la fecha de vencimiento.
 * Puede ser negativo si el producto ya venció.
 * @property fechaLimiteCanje Primer día del mes de vencimiento del producto,
 * utilizado como fecha límite para gestionar el canje ante el laboratorio.
 * Null si el producto no es CANJEABLE.
 */
data class ResultadoClasificacion(
    val producto: Producto,
    val nombreLaboratorio: String,
    val fechaVencimiento: LocalDate,
    val clasificacion: Clasificacion,
    val diasRestantes: Long,
    val fechaLimiteCanje: LocalDate?
)

/**
 * Estados posibles de clasificación de un producto farmacéutico.
 *
 * - [VIGENTE]: el producto está dentro de su período de validez y no aplica canje.
 * - [CANJEABLE]: el mes de vencimiento coincide con el período de canje activo
 *   del laboratorio. El producto debe separarse del inventario para gestionar
 *   su devolución.
 * - [VENCIDO]: el producto ha superado su fecha de vencimiento y debe retirarse
 *   del inventario como merma.
 */
enum class Clasificacion {
    VIGENTE,
    CANJEABLE,
    VENCIDO
}

/**
 * Motor de clasificación de productos farmacéuticos.
 *
 * Objeto singleton que implementa la lógica central de clasificación del sistema.
 * Determina el estado de un producto a partir de su fecha de vencimiento y la
 * política de canje vigente del laboratorio fabricante, evaluando cuatro escenarios
 * mutuamente excluyentes en orden de prioridad:
 *
 * 1. Sin política registrada → VENCIDO si fechaActual >= fechaVencimiento, sino VIGENTE.
 * 2. Política con [PoliticaCanje.vencimiento] = false → siempre VIGENTE.
 * 3. Política activa y producto vencido → VENCIDO.
 * 4. Mes de vencimiento dentro del período de canje activo → CANJEABLE, sino VIGENTE.
 *
 * La comparación se realiza por [YearMonth] en lugar de fecha completa, dado que
 * las fechas de vencimiento en medicamentos se expresan únicamente como mes y año.
 *
 * El parámetro [fechaActual] admite inyección para facilitar la verificación del
 * comportamiento con fechas arbitrarias sin depender del reloj del sistema.
 */
object ClasificadorProducto {

    /**
     * Clasifica un producto farmacéutico según su fecha de vencimiento y la
     * política de canje de su laboratorio.
     *
     * @param producto Producto a clasificar.
     * @param nombreLaboratorio Nombre del laboratorio fabricante.
     * @param fechaVencimiento Fecha de vencimiento confirmada por el operador.
     * @param politica Política de canje del laboratorio, o null si no existe.
     * @param fechaActual Fecha de referencia para la clasificación. Por defecto
     * es la fecha actual del sistema ([LocalDate.now]).
     * @return [ResultadoClasificacion] con el estado asignado y los datos
     * necesarios para la interfaz de usuario.
     */
    fun clasificar(
        producto: Producto,
        nombreLaboratorio: String,
        fechaVencimiento: LocalDate,
        politica: PoliticaCanje?,
        fechaActual: LocalDate = LocalDate.now()
    ): ResultadoClasificacion {

        val diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(fechaActual, fechaVencimiento)

        // Escenario 1: sin política → clasificar solo por fecha de vencimiento
        if (politica == null) {
            val clasificacion = if (fechaActual >= fechaVencimiento) Clasificacion.VENCIDO
            else Clasificacion.VIGENTE
            return ResultadoClasificacion(
                producto = producto,
                nombreLaboratorio = nombreLaboratorio,
                fechaVencimiento = fechaVencimiento,
                clasificacion = clasificacion,
                diasRestantes = diasRestantes,
                fechaLimiteCanje = null
            )
        }

        // Escenario 2: política sin canje por vencimiento → siempre VIGENTE
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

        // Escenario 3: política activa y producto vencido → VENCIDO (merma)
        if (fechaActual >= fechaVencimiento) {
            return ResultadoClasificacion(
                producto = producto,
                nombreLaboratorio = nombreLaboratorio,
                fechaVencimiento = fechaVencimiento,
                clasificacion = Clasificacion.VENCIDO,
                diasRestantes = diasRestantes,
                fechaLimiteCanje = null
            )
        }

        // Escenario 4: verificar si el mes de vencimiento está en el período de canje activo
        val periodoVencimiento = YearMonth.from(fechaVencimiento)
        val mesesPolitica = listOfNotNull(
            politica.mesUno?.let  { YearMonth.from(it) },
            politica.mesDos?.let  { YearMonth.from(it) },
            politica.mesTres?.let { YearMonth.from(it) }
        )

        val esCanjeable = periodoVencimiento in mesesPolitica
        val fechaLimiteCanje = if (esCanjeable) fechaVencimiento.withDayOfMonth(1) else null
        val clasificacion = if (esCanjeable) Clasificacion.CANJEABLE else Clasificacion.VIGENTE

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