package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa la empresa dueña del laboratorio.
 *
 * @property id Identificador único de la empresa (auto-generado).
 * @property razonSocial Nombre legal de la empresa.
 */
@Entity(tableName = "empresas")
data class Empresa(
    @PrimaryKey
    val id: Int,
    val razonSocial: String
)
