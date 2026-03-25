package com.lepeman.pharmadatecheck.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    .addCallback(AppDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val context:Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        prepoblarDatos(database)
                    }
                }
            }

            suspend fun prepoblarDatos(db: AppDatabase) {
                val laboratorioDao = db.laboratorioDao()
                val listaLaboratorios = listOf(
                    Laboratorio(id = 1, nombre = "CHILE RECETARIO"),
                    Laboratorio(id = 2, nombre = "CHILEMARCAS"),
                    Laboratorio(id = 3, nombre = "BAYER POPULAR"),
                    Laboratorio(id = 4, nombre = "RECALCINE"),
                    Laboratorio(id = 5, nombre = "ASSISTANCE")
                )

                laboratorioDao.insertarTodosLosLaboratorios(listaLaboratorios)

                val empresaDao = db.empresaDao()
                val listaEmpresas = listOf(
                    Empresa(id = 1, razonSocial="PHARMATRADE S.A."),
                    Empresa(id = 2, razonSocial="BAYER S.A."),
                    Empresa(id = 3, razonSocial="ABBOT LABORATORIES DE CHILE S.A."),
                    Empresa(id = 4, razonSocial="LABORATORIOS RECALCINE S.A."),
                    Empresa(id = 5, razonSocial="SOCIEDAD COMERCIAL ASSITANCE OTC CHILE LTDA."),
                    Empresa(id = 6, razonSocial="LABORATORIOS SAVAL S.A."),
                    Empresa(id = 7, razonSocial="ASTRAZENECA S.A."),
                    Empresa(id = 8, razonSocial="NOVARTIS CHILE S.A."),
                    Empresa(id = 9, razonSocial="NOVOFARMA SERVICE S.A."),
                    Empresa(id = 10, razonSocial="MERCK S.A."),
                    Empresa(id = 11, razonSocial="CHEMOPHARMA S.A."),
                    Empresa(id = 12, razonSocial="INSTITUTO SANITAS S.A.")
                )
                
                empresaDao.insertarTodasLasEmpresas(listaEmpresas)

                val auxiliarDao = db.auxiliarDao()
                val listaAuxiliares = listOf(
                    Auxiliar(1, "Juan Pérez", "123456789"),
                    Auxiliar(2, "María González", "15672341k"),
                    Auxiliar(3, "Carlos Muñoz", "189012345"),
                    Auxiliar(4, "Ana Silva", "104328767"),
                    Auxiliar(5, "Roberto Tapia", "145567890"),
                    Auxiliar(6, "Elena Morales", "172234456"),
                    Auxiliar(7, "Pedro Soto", "98765432"),
                    Auxiliar(8, "Lucía Herrera", "201123341"),
                    Auxiliar(9, "Luis Ortega", "151748309")
                )

                auxiliarDao.insertarTodosLosAuxiliares(listaAuxiliares)
                
            }

        }
    }
}
