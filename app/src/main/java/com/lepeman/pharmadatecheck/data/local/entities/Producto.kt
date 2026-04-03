package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad que representa un producto farmacéutico del catálogo.
 *
 * El catálogo de productos se carga mediante importación CSV desde la
 * pantalla de configuración. El [codigoEAN13] actúa como clave primaria
 * natural del producto, dado que es el identificador único asignado por
 * el estándar GS1 y es el dato capturado por el lector HID durante el
 * proceso de revisión.
 *
 * La relación con [Laboratorio] utiliza comportamiento SET_DEFAULT: si el
 * laboratorio referenciado es eliminado, el campo [laboratorioId] del producto
 * se establece en 0 en lugar de eliminar el registro. El valor 0 representa
 * convencionalmente "sin laboratorio asignado" y es el valor por defecto del
 * campo. Esta decisión preserva el producto en el catálogo ante la eliminación
 * del laboratorio, evitando pérdida de datos históricos en el registro de
 * productos revisados.
 *
 * @property codigoEAN13 Código de barras EAN-13 del producto. Actúa como
 * clave primaria natural e identificador único en el catálogo.
 * @property nombre Nombre comercial o genérico del producto farmacéutico.
 * @property laboratorioId Identificador del laboratorio fabricante. El valor
 * 0 indica que el producto no tiene laboratorio asignado. Clave foránea hacia
 * [Laboratorio] con onDelete SET_DEFAULT.
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