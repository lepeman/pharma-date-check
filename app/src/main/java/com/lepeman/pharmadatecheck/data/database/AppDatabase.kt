package com.lepeman.pharmadatecheck.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lepeman.pharmadatecheck.data.local.dao.AuxiliarDao
import com.lepeman.pharmadatecheck.data.local.dao.EmpresaDao
import com.lepeman.pharmadatecheck.data.local.dao.LaboratorioDao
import com.lepeman.pharmadatecheck.data.local.dao.PoliticaCanjeDao
import com.lepeman.pharmadatecheck.data.local.dao.ProductoDao
import com.lepeman.pharmadatecheck.data.local.dao.ProductoRevisadoDao
import com.lepeman.pharmadatecheck.data.local.dao.SesionRevisionDao
import com.lepeman.pharmadatecheck.data.local.entities.Auxiliar
import com.lepeman.pharmadatecheck.data.local.entities.Empresa
import com.lepeman.pharmadatecheck.data.local.entities.Laboratorio
import com.lepeman.pharmadatecheck.data.local.entities.PoliticaCanje
import com.lepeman.pharmadatecheck.data.local.entities.Producto
import com.lepeman.pharmadatecheck.data.local.entities.ProductoRevisado
import com.lepeman.pharmadatecheck.data.local.entities.SesionRevision

/**
 * Base de datos principal de la aplicación utilizando Room.
 * Define las entidades que componen la base de datos y proporciona acceso a los DAOs.
 */
@Database(
    entities = [
        Auxiliar::class,
        Producto::class,
        Empresa::class,
        Laboratorio::class,
        PoliticaCanje::class,
        ProductoRevisado::class,
        SesionRevision::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // Utiliza conversores para tipos de datos complejos (como LocalDateTime)
abstract class AppDatabase : RoomDatabase() {

    // Métodos abstractos para obtener las interfaces de acceso a datos (DAOs)
    abstract fun auxiliarDao(): AuxiliarDao
    abstract fun productoDao(): ProductoDao
    abstract fun empresaDao(): EmpresaDao
    abstract fun laboratorioDao(): LaboratorioDao
    abstract fun politicaCanjeDao(): PoliticaCanjeDao
    abstract fun productoRevisadoDao(): ProductoRevisadoDao
    abstract fun sesionRevisionDao(): SesionRevisionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos (Patrón Singleton).
         * Si la base de datos no existe, se crea utilizando Room.databaseBuilder.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vencimientos_farmacia.db"
                )
                    // Política de migración destructiva (opcional: borrar y recrear si cambia la versión)
                    // .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
