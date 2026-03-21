package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad que representa un producto farmacéutico.
 *
 * @property codigoEAN13 Código de barras único del producto (EAN-13).
 * @property nombre Nombre comercial o genérico del producto.
 * @property laboratorioId Identificador del laboratorio fabricante. 0 indica "Sin laboratorio asignado".
 */
@Entity(
    tableName = "productos",
    foreignKeys = [
        ForeignKey(
            entity = Laboratorio::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.SET_DEFAULT
        )
    ],
    indices = [Index("laboratorioId")]
)
data class Producto(
    @PrimaryKey
    val codigoEAN13: String,
    val nombre: String,
    val laboratorioId: Int = 0
)
