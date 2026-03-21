package com.lepeman.pharmadatecheck.data.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Clase de utilidad para convertir tipos de datos complejos en tipos que Room pueda almacenar.
 * 
 * Room no puede almacenar directamente tipos como [LocalDate] o [LocalDateTime].
 * Estos conversores transforman dichos objetos en [String] para su almacenamiento y viceversa.
 */
class Converters {
    
    /**
     * Convierte un objeto [LocalDate] a su representación en [String].
     */
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    /**
     * Convierte una cadena de texto [String] de vuelta a un objeto [LocalDate].
     */
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    /**
     * Convierte un objeto [LocalDateTime] a su representación en [String].
     */
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? = value?.toString()

    /**
     * Convierte una cadena de texto [String] de vuelta a un objeto [LocalDateTime].
     */
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }
}
