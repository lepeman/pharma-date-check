package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Entidad que representa una sesión de revisión de inventario farmacéutico.
 *
 * Una sesión agrupa todos los productos revisados por un auxiliar durante
 * un período continuo de trabajo. Se crea al autenticarse el auxiliar en
 * la pantalla de escaneo y se cierra explícitamente mediante el botón
 * "Cerrar sesión", momento en que se registran [fechaTermino] y los totales
 * acumulados de clasificación.
 *
 * La relación con [Auxiliar] utiliza comportamiento CASCADE: si el auxiliar
 * es eliminado del sistema, sus sesiones asociadas se eliminan también.
 * A su vez, [ProductoRevisado] mantiene una relación CASCADE hacia esta
 * entidad, de modo que eliminar una sesión elimina automáticamente todos
 * sus productos revisados.
 *
 * @property id Identificador único de la sesión, autoGenerado por Room.
 * @property auxiliarId Referencia al auxiliar responsable de la sesión.
 * Clave foránea hacia [Auxiliar] con onDelete CASCADE.
 * @property fechaInicio Fecha y hora en que el auxiliar inició la sesión.
 * @property fechaTermino Fecha y hora en que se cerró la sesión. Null
 * mientras la sesión permanece activa.
 * @property totalVigentes Cantidad acumulada de productos clasificados
 * como VIGENTE durante la sesión.
 * @property totalCanjeables Cantidad acumulada de productos clasificados
 * como CANJEABLE durante la sesión.
 * @property totalVencidos Cantidad acumulada de productos clasificados
 * como VENCIDO durante la sesión.
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
    val totalVencidos: Int = 0
)