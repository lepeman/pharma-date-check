package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Entidad que registra el resultado de la revisión de un producto específico.
 *
 * @property id Identificador único del registro (auto-generado).
 * @property sesionId Referencia a la sesión de revisión a la que pertenece este registro.
 * @property productoId Referencia al producto que fue revisado.
 * @property clasificacion Estado del producto tras la revisión (ej: "Vigente", "Canjeable", "Vencido").
 * @property timestamp Fecha y hora exacta en la que se realizó la revisión del producto.
 */
@Entity(tableName = "productos_revisados")
data class ProductoRevisado(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val sesionId: Int,
    val productoId: Int,
    val clasificacion: String,
    val timestamp: LocalDateTime
)
