package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Entidad que define la política de canje vigente para un laboratorio específico.
 *
 * Las políticas de canje son comunicadas periódicamente por los laboratorios
 * como períodos de tres meses dentro de los cuales los productos con fecha de
 * vencimiento comprendida en esos meses son elegibles para retiro y compensación.
 * Esta entidad modela esa realidad mediante tres atributos de fecha opcionales
 * ([mesUno], [mesDos], [mesTres]) que representan dichos meses.
 *
 * Convención de fechas: cada mes se almacena como un [LocalDate] con día fijo
 * igual a 1 (por ejemplo, 01/06/2026 representa el mes de junio 2026). La
 * interfaz de usuario muestra y solicita únicamente mes y año, ocultando este
 * detalle de implementación al operador.
 *
 * La relación con [Empresa] y [Laboratorio] utiliza comportamiento CASCADE,
 * de modo que al eliminar cualquiera de ellos se eliminan también las políticas
 * asociadas. El motor de clasificación [ClasificadorProducto] consulta esta
 * entidad para determinar si un producto escaneado es CANJEABLE.
 *
 * @property id Identificador único de la política, autoGenerado por Room.
 * @property empresaId Referencia a la razón social del laboratorio que emite
 * la política. Clave foránea hacia [Empresa] con onDelete CASCADE.
 * @property laboratorioId Referencia al laboratorio al que aplica la política.
 * Clave foránea hacia [Laboratorio] con onDelete CASCADE.
 * @property vencimiento Indica si el laboratorio permite canje por vencimiento.
 * Si es false, los productos de este laboratorio siempre clasifican como VIGENTE
 * independientemente de su fecha de vencimiento.
 * @property mesUno Primer mes del período de canje activo, con día fijo = 1.
 * Null si no aplica o no ha sido configurado.
 * @property mesDos Segundo mes del período de canje activo, con día fijo = 1.
 * Null si no aplica o no ha sido configurado.
 * @property mesTres Tercer mes del período de canje activo, con día fijo = 1.
 * Null si no aplica o no ha sido configurado.
 */
@Entity(
    tableName = "politicas_canje",
    foreignKeys = [
        ForeignKey(
            entity = Empresa::class,
            parentColumns = ["id"],
            childColumns = ["empresaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Laboratorio::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("empresaId"),
        Index("laboratorioId")
    ]
)
data class PoliticaCanje(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val empresaId: Int,
    val laboratorioId: Int,
    val vencimiento: Boolean,
    val mesUno: LocalDate?,
    val mesDos: LocalDate?,
    val mesTres: LocalDate?
)