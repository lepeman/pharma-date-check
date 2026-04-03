package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa la razón social legal de un laboratorio farmacéutico.
 *
 * Un mismo laboratorio puede operar bajo múltiples razones sociales según
 * el tipo de producto o canal de distribución. Por ello, la relación entre
 * [Laboratorio] y [Empresa] es de uno a muchos. Las políticas de canje se
 * establecen por razón social y no por laboratorio, lo que hace necesaria
 * esta distinción en el modelo de datos.
 *
 * El catálogo de empresas se carga mediante prepoblado automático en
 * [AppDatabase] al primer inicio de la aplicación. El [id] es asignado
 * manualmente en ese proceso y no es autoGenerado.
 *
 * @property id Identificador único de la empresa, asignado manualmente.
 * @property razonSocial Nombre legal de la empresa ante la autoridad sanitaria.
 */
@Entity(tableName = "empresas")
data class Empresa(
    @PrimaryKey
    val id: Int,
    val razonSocial: String
)