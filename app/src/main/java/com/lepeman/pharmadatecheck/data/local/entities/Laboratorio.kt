package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un laboratorio farmacéutico.
 *
 * @property id Identificador único del laboratorio (proveído por sistema externo).
 * @property nombre Nombre comercial del laboratorio.
 */
@Entity(tableName = "laboratorios")
data class Laboratorio(
    @PrimaryKey
    val id: Int,
    val nombre: String
)
