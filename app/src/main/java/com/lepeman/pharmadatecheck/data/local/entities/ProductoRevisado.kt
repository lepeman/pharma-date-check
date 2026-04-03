package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Entidad que registra el resultado de la revisión de un producto durante
 * una sesión de inventario.
 *
 * Cada registro asocia un código EAN-13 escaneado a una sesión de revisión
 * activa, almacenando la clasificación asignada por [ClasificadorProducto]
 * y el timestamp exacto del escaneo. La relación con [SesionRevision] utiliza
 * comportamiento CASCADE, de modo que al eliminar una sesión se eliminan
 * automáticamente todos sus productos revisados asociados.
 *
 * El campo [codigoEAN13] no es una clave foránea formal hacia [Producto]
 * sino una referencia lógica, dado que un producto podría ser escaneado
 * aunque no esté registrado en el catálogo local.
 *
 * @property id Identificador único del registro, autoGenerado por Room.
 * @property sesionId Referencia a la sesión de revisión a la que pertenece
 * este registro. Clave foránea hacia [SesionRevision] con onDelete CASCADE.
 * @property codigoEAN13 Código EAN-13 del producto revisado. Referencia
 * lógica al catálogo de productos, sin restricción de integridad referencial.
 * @property clasificacion Resultado de la clasificación asignada por el motor:
 * "VIGENTE", "CANJEABLE" o "VENCIDO".
 * @property timestamp Fecha y hora exacta en que se realizó el escaneo.
 */
@Entity(
    tableName = "productos_revisados",
    foreignKeys = [
        ForeignKey(
            entity = SesionRevision::class,
            parentColumns = ["id"],
            childColumns = ["sesionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sesionId")]
)
data class ProductoRevisado(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sesionId: Int,
    val codigoEAN13: String,
    val clasificacion: String,
    val timestamp: LocalDateTime
)