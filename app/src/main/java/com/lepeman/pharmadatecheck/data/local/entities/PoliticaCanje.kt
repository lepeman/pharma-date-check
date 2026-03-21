package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Entidad que define la política de canje para un laboratorio específico.
 *
 * @property id Identificador único de la política (auto-generado).
 * @property empresaId Referencia a la empresa (dueña de la farmacia).
 * @property laboratorioId Referencia al laboratorio al que aplica la política.
 * @property vencimiento Indica si se permite canje por vencimiento (true) o no (false).
 * @property mesUno Fecha límite o hito 1 para la política de canje.
 * @property mesDos Fecha límite o hito 2 para la política de canje.
 * @property mesTres Fecha límite o hito 3 para la política de canje.
 */
@Entity(tableName = "politicas_canje")
data class PoliticaCanje(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val empresaId: Int,
    val laboratorioId: Int,
    val vencimiento: Boolean,
    val mesUno: LocalDate?,
    val mesDos: LocalDate?,
    val mesTres: LocalDate?
)
