package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa a un auxiliar de farmacia.
 *
 * @property id Identificador único del auxiliar (auto-generado).
 * @property nombreAuxiliar Nombre completo del auxiliar.
 * @property rutAuxiliar Rol Único Tributario (RUT) del auxiliar para identificación.
 */
@Entity(tableName = "auxiliares")
data class Auxiliar(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombreAuxiliar: String,
    val rutAuxiliar: String
)
