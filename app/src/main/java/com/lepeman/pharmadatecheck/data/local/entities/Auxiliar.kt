package com.lepeman.pharmadatecheck.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa a un auxiliar de farmacia habilitado para
 * operar la aplicación.
 *
 * El catálogo de auxiliares se carga mediante prepoblado automático
 * en [AppDatabase] al primer inicio de la aplicación. El [id] es
 * asignado manualmente en ese proceso y no es autoGenerado, dado que
 * corresponde a un identificador controlado por el establecimiento.
 *
 * @property id Identificador único del auxiliar, asignado manualmente.
 * @property nombreAuxiliar Nombre completo del auxiliar de farmacia.
 * @property rutAuxiliar RUT del auxiliar, usado como credencial de
 * autenticación al iniciar una sesión de revisión.
 */
@Entity(tableName = "auxiliares")
data class Auxiliar(
    @PrimaryKey
    val id: Int,
    val nombreAuxiliar: String,
    val rutAuxiliar: String
)