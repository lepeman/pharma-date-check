package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Entidad que representa una sesión de revisión de inventario.
 *
 * @property id Identificador único de la sesión (auto-generado).
 * @property auxiliarId Identificador del auxiliar que realiza la revisión.
 * @property fechaInicio Fecha y hora en que se inicia la sesión.
 * @property fechaTermino Fecha y hora en que se finaliza la sesión (opcional).
 * @property totalVigentes Cantidad total de productos marcados como vigentes en esta sesión.
 * @property totalCanjeables Cantidad total de productos marcados como canjeables en esta sesión.
 * @property totalVencidos Cantidad total de productos marcados como vencidos en esta sesión.
 */
@Entity(
    tableName = "sesiones_revision",
    foreignKeys = [
        ForeignKey(
            entity = Auxiliar::class,
            parentColumns = ["id"],
            childColumns = ["auxiliarId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("auxiliarId")]
)
data class SesionRevision(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val auxiliarId: Int,
    val fechaInicio: LocalDateTime,
    val fechaTermino: LocalDateTime? = null,
    val totalVigentes: Int = 0,
    val totalCanjeables: Int = 0,
    val totalVencidos: Int = 0,
)
