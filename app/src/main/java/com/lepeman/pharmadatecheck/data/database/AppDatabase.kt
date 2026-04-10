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
 * Base de datos principal de la aplicación, implementada con Room.
 *
 * Declara las siete entidades del modelo de datos y registra los conversores
 * de tipos necesarios para almacenar [java.time.LocalDate] y
 * [java.time.LocalDateTime] en SQLite. Al crearse por primera vez, ejecuta
 * automáticamente el prepoblado de laboratorios, empresas y auxiliares
 * mediante [AppDatabaseCallback], dejando la aplicación en estado operativo
 * desde el primer arranque sin intervención del usuario.
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
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun auxiliarDao(): AuxiliarDao
    abstract fun productoDao(): ProductoDao
    abstract fun empresaDao(): EmpresaDao
    abstract fun laboratorioDao(): LaboratorioDao
    abstract fun politicaCanjeDao(): PoliticaCanjeDao
    abstract fun productoRevisadoDao(): ProductoRevisadoDao
    abstract fun sesionRevisionDao(): SesionRevisionDao

    companion object {

        // Volatile garantiza que los cambios sobre INSTANCE sean visibles
        // inmediatamente a todos los hilos, evitando instancias duplicadas.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Retorna la instancia única de la base de datos (patrón Singleton).
         *
         * Si la instancia no existe, la crea con [Room.databaseBuilder] dentro
         * de un bloque sincronizado para prevenir condiciones de carrera en
         * entornos multihilo.
         *
         * @param context Contexto de la aplicación para localizar el archivo
         * de base de datos en el almacenamiento del dispositivo.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vencimientos_farmacia.db"
                )
                    .addCallback(AppDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Callback que se ejecuta una única vez cuando la base de datos es
         * creada por primera vez en el dispositivo.
         *
         * Aprovecha el evento [onCreate] para insertar los datos iniciales
         * necesarios para la operación de la aplicación: catálogo de
         * laboratorios, razones sociales (empresas) y auxiliares de farmacia
         * habilitados. La inserción se realiza en el dispatcher IO para no
         * bloquear el hilo principal.
         */
        private class AppDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        prepoblarDatos(database)
                    }
                }
            }

            /**
             * Inserta los datos iniciales en las tablas de laboratorios,
             * empresas y auxiliares.
             *
             * Estos datos corresponden al catálogo base de la sucursal CV105
             * de Farmacias Cruz Verde y deben mantenerse actualizados conforme
             * se incorporen nuevos laboratorios, razones sociales o personal
             * al establecimiento.
             *
             * @param db Instancia de [AppDatabase] ya inicializada por Room.
             */
            private suspend fun prepoblarDatos(db: AppDatabase) {

                // ── Laboratorios ──────────────────────────────────────────────
                db.laboratorioDao().insertarTodosLosLaboratorios(listOf(
                    Laboratorio(id = 1, nombre = "CHILE RECETARIO"),
                    Laboratorio(id = 2, nombre = "CHILEMARCAS"),
                    Laboratorio(id = 3, nombre = "BAYER POPULAR"),
                    Laboratorio(id = 4, nombre = "RECALCINE"),
                    Laboratorio(id = 5, nombre = "ASSISTANCE"),
                    Laboratorio(id = 1190, nombre = "SEVEN PHARMA")
                ))

                // ── Empresas (razones sociales) ───────────────────────────────
                db.empresaDao().insertarTodasLasEmpresas(listOf(
                    Empresa(id = 1,  razonSocial = "PHARMATRADE S.A."),
                    Empresa(id = 2,  razonSocial = "BAYER S.A."),
                    Empresa(id = 3,  razonSocial = "ABBOT LABORATORIES DE CHILE S.A."),
                    Empresa(id = 4,  razonSocial = "LABORATORIOS RECALCINE S.A."),
                    Empresa(id = 5,  razonSocial = "SOCIEDAD COMERCIAL ASSITANCE OTC CHILE LTDA."),
                    Empresa(id = 6,  razonSocial = "LABORATORIOS SAVAL S.A."),
                    Empresa(id = 7,  razonSocial = "ASTRAZENECA S.A."),
                    Empresa(id = 8,  razonSocial = "NOVARTIS CHILE S.A."),
                    Empresa(id = 9,  razonSocial = "NOVOFARMA SERVICE S.A."),
                    Empresa(id = 10, razonSocial = "MERCK S.A."),
                    Empresa(id = 11, razonSocial = "CHEMOPHARMA S.A."),
                    Empresa(id = 12, razonSocial = "INSTITUTO SANITAS S.A."),
                    Empresa(id = 13, razonSocial = "SEVEN PHARMA S.P.A.")
                ))

                // ── Auxiliares de farmacia ────────────────────────────────────
                db.auxiliarDao().insertarTodosLosAuxiliares(listOf(
                    Auxiliar(id = 1, nombreAuxiliar = "Juan Pérez",     rutAuxiliar = "123456789"),
                    Auxiliar(id = 2, nombreAuxiliar = "María González", rutAuxiliar = "15672341k"),
                    Auxiliar(id = 3, nombreAuxiliar = "Carlos Muñoz",   rutAuxiliar = "189012345"),
                    Auxiliar(id = 4, nombreAuxiliar = "Ana Silva",      rutAuxiliar = "104328767"),
                    Auxiliar(id = 5, nombreAuxiliar = "Roberto Tapia",  rutAuxiliar = "145567890"),
                    Auxiliar(id = 6, nombreAuxiliar = "Elena Morales",  rutAuxiliar = "172234456"),
                    Auxiliar(id = 7, nombreAuxiliar = "Pedro Soto",     rutAuxiliar = "98765432"),
                    Auxiliar(id = 8, nombreAuxiliar = "Lucía Herrera",  rutAuxiliar = "201123341"),
                    Auxiliar(id = 9, nombreAuxiliar = "Luis Ortega",    rutAuxiliar = "151748309")
                ))
            }
        }
    }
}