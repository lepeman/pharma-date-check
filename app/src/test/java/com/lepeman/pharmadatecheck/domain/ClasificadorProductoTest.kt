package com.lepeman.pharmadatecheck.domain

import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

/**
 * Pruebas unitarias del motor de clasificación [ClasificadorProducto].
 *
 * Todas las pruebas inyectan [HOY] como fecha actual, por lo que los resultados
 * no dependen del reloj del sistema.
 */
class ClasificadorProductoTest {

    private companion object {
        val HOY: LocalDate = LocalDate.of(2026, 3, 15)
        const val LABORATORIO = "LAB DE PRUEBA"
        val PRODUCTO = Producto(codigoEAN13 = "7800000000001", nombre = "PRODUCTO DE PRUEBA", laboratorioId = 1)
    }

    /** Crea una política de canje de prueba; por defecto, activa y sin meses definidos. */
    private fun politica(
        vencimiento: Boolean = true,
        mesUno: LocalDate? = null,
        mesDos: LocalDate? = null,
        mesTres: LocalDate? = null
    ) = PoliticaCanje(
        empresaId = 1,
        laboratorioId = 1,
        vencimiento = vencimiento,
        mesUno = mesUno,
        mesDos = mesDos,
        mesTres = mesTres
    )

    private fun clasificar(fechaVencimiento: LocalDate, politica: PoliticaCanje?) =
        ClasificadorProducto.clasificar(
            producto = PRODUCTO,
            nombreLaboratorio = LABORATORIO,
            fechaVencimiento = fechaVencimiento,
            politica = politica,
            fechaActual = HOY
        )

    // ── Escenario 1: sin política ─────────────────────────────────────────────

    @Test
    fun sinPolitica_fechaFutura_esVigente() {
        val resultado = clasificar(LocalDate.of(2027, 6, 30), politica = null)

        assertEquals(Clasificacion.VIGENTE, resultado.clasificacion)
        assertNull(resultado.fechaLimiteCanje)
    }

    @Test
    fun sinPolitica_fechaPasada_esVencido() {
        val resultado = clasificar(LocalDate.of(2026, 1, 31), politica = null)

        assertEquals(Clasificacion.VENCIDO, resultado.clasificacion)
        assertNull(resultado.fechaLimiteCanje)
    }

    @Test
    fun sinPolitica_venceElMismoDia_esVencido() {
        val resultado = clasificar(HOY, politica = null)

        assertEquals(Clasificacion.VENCIDO, resultado.clasificacion)
    }

    // ── Escenario 2: política sin canje por vencimiento ───────────────────────

    @Test
    fun politicaSinCanjePorVencimiento_fechaFutura_esVigenteAunqueCoincidaElMes() {
        val politica = politica(vencimiento = false, mesUno = LocalDate.of(2026, 5, 1))

        val resultado = clasificar(LocalDate.of(2026, 5, 31), politica)

        assertEquals(Clasificacion.VIGENTE, resultado.clasificacion)
        assertNull(resultado.fechaLimiteCanje)
    }

    // ── Escenario 3: política activa y producto vencido ───────────────────────

    @Test
    fun politicaActiva_productoVencido_esVencido() {
        val politica = politica(mesUno = LocalDate.of(2026, 3, 1))

        val resultado = clasificar(LocalDate.of(2026, 3, 1), politica)

        assertEquals(Clasificacion.VENCIDO, resultado.clasificacion)
        assertNull(resultado.fechaLimiteCanje)
    }

    // ── Escenario 4: período de canje ─────────────────────────────────────────

    @Test
    fun politicaActiva_mesUnoCoincide_esCanjeable() {
        val politica = politica(mesUno = LocalDate.of(2026, 5, 1))

        val resultado = clasificar(LocalDate.of(2026, 5, 31), politica)

        assertEquals(Clasificacion.CANJEABLE, resultado.clasificacion)
    }

    @Test
    fun politicaActiva_mesDosCoincide_esCanjeable() {
        val politica = politica(
            mesUno = LocalDate.of(2026, 5, 1),
            mesDos = LocalDate.of(2026, 6, 1)
        )

        val resultado = clasificar(LocalDate.of(2026, 6, 30), politica)

        assertEquals(Clasificacion.CANJEABLE, resultado.clasificacion)
    }

    @Test
    fun politicaActiva_mesTresCoincide_esCanjeable() {
        val politica = politica(
            mesUno = LocalDate.of(2026, 5, 1),
            mesDos = LocalDate.of(2026, 6, 1),
            mesTres = LocalDate.of(2026, 7, 1)
        )

        val resultado = clasificar(LocalDate.of(2026, 7, 15), politica)

        assertEquals(Clasificacion.CANJEABLE, resultado.clasificacion)
    }

    @Test
    fun canjeable_fechaLimiteEsElPrimerDiaDelMesDeVencimiento() {
        val politica = politica(mesUno = LocalDate.of(2026, 5, 1))

        val resultado = clasificar(LocalDate.of(2026, 5, 31), politica)

        assertEquals(LocalDate.of(2026, 5, 1), resultado.fechaLimiteCanje)
    }

    @Test
    fun politicaActiva_mesNoIncluido_esVigente() {
        val politica = politica(
            mesUno = LocalDate.of(2026, 5, 1),
            mesDos = LocalDate.of(2026, 6, 1)
        )

        val resultado = clasificar(LocalDate.of(2026, 9, 30), politica)

        assertEquals(Clasificacion.VIGENTE, resultado.clasificacion)
        assertNull(resultado.fechaLimiteCanje)
    }

    @Test
    fun politicaActiva_mismoMesPeroOtroAnio_esVigente() {
        val politica = politica(mesUno = LocalDate.of(2026, 5, 1))

        val resultado = clasificar(LocalDate.of(2027, 5, 31), politica)

        assertEquals(Clasificacion.VIGENTE, resultado.clasificacion)
    }

    @Test
    fun politicaActiva_sinMesesDefinidos_esVigente() {
        val resultado = clasificar(LocalDate.of(2026, 8, 31), politica())

        assertEquals(Clasificacion.VIGENTE, resultado.clasificacion)
        assertNull(resultado.fechaLimiteCanje)
    }

    // ── Datos del resultado ───────────────────────────────────────────────────

    @Test
    fun diasRestantes_fechaFutura_esPositivo() {
        val resultado = clasificar(LocalDate.of(2026, 4, 14), politica = null)

        assertEquals(30L, resultado.diasRestantes)
    }

    @Test
    fun diasRestantes_fechaPasada_esNegativo() {
        val resultado = clasificar(LocalDate.of(2026, 3, 5), politica = null)

        assertEquals(-10L, resultado.diasRestantes)
    }

    @Test
    fun resultado_conservaProductoLaboratorioYFechaDeVencimiento() {
        val vencimiento = LocalDate.of(2027, 1, 31)

        val resultado = clasificar(vencimiento, politica = null)

        assertEquals(PRODUCTO, resultado.producto)
        assertEquals(LABORATORIO, resultado.nombreLaboratorio)
        assertEquals(vencimiento, resultado.fechaVencimiento)
    }
}
