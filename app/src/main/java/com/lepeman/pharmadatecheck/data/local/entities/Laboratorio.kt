package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un laboratorio farmacéutico del catálogo de Cruz Verde.
 *
 * El [id] corresponde al código numérico asignado por el sistema de inventario
 * de Cruz Verde, por lo que no es autoGenerado sino provisto externamente. Esto
 * garantiza consistencia entre la base de datos local de la aplicación y el
 * sistema corporativo, facilitando una eventual integración futura.
 *
 * [Laboratorio] es referenciado por [Producto] mediante clave foránea con
 * comportamiento SET_DEFAULT, y por [PoliticaCanje] con comportamiento CASCADE.
 * Su catálogo puede ser actualizado mediante importación CSV desde la pantalla
 * de configuración, o bien mediante el prepoblado automático de [AppDatabase]
 * al primer inicio de la aplicación.
 *
 * @property id Código numérico del laboratorio provisto por el sistema de
 * inventario de Cruz Verde.
 * @property nombre Nombre comercial del laboratorio farmacéutico.
 */
@Entity(tableName = "laboratorios")
data class Laboratorio(
    @PrimaryKey
    val id: Int,
    val nombre: String
)