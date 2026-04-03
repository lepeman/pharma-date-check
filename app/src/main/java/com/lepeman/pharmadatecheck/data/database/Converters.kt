package com.lepeman.pharmadatecheck.data.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Conversores de tipos para Room.
 *
 * Room únicamente puede persistir tipos primitivos y [String] de forma nativa.
 * Esta clase registra los conversores necesarios para serializar [LocalDate] y
 * [LocalDateTime] como cadenas ISO-8601 (por ejemplo, "2026-06-01" y
 * "2026-06-01T14:30:00") antes de escribirlas en SQLite, y deserializarlas
 * al leerlas. Se eligió [String] sobre [Long] (epoch) porque el formato
 * ISO-8601 es legible directamente en el archivo de base de datos, lo que
 * facilita la depuración sin herramientas adicionales.
 *
 * Room detecta y aplica estos conversores automáticamente gracias a la
 * anotación [@TypeConverters(Converters::class)] declarada en [AppDatabase].
 */
class Converters {

    /**
     * Serializa un [LocalDate] a su representación ISO-8601 ("yyyy-MM-dd").
     * Retorna null si el valor de entrada es null.
     */
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    /**
     * Deserializa una cadena ISO-8601 ("yyyy-MM-dd") a [LocalDate].
     * Retorna null si la cadena de entrada es null.
     */
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    /**
     * Serializa un [LocalDateTime] a su representación ISO-8601
     * ("yyyy-MM-ddTHH:mm:ss").
     * Retorna null si el valor de entrada es null.
     */
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? = value?.toString()

    /**
     * Deserializa una cadena ISO-8601 ("yyyy-MM-ddTHH:mm:ss") a [LocalDateTime].
     * Retorna null si la cadena de entrada es null.
     */
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }
}