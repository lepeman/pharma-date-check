package com.lepeman.pharmadatecheck

import android.app.Application
import com.lepeman.pharmadatecheck.data.database.container.AppContainer
import com.lepeman.pharmadatecheck.data.database.container.AppDataContainer

/**
 * Clase principal de la aplicación que extiende de [Application].
 * Se encarga de la configuración global y de inicializar el contenedor de dependencias.
 */
class DateCheckApplication : Application() {

    /**
     * Instancia de [AppContainer] utilizada por el resto de la aplicación para obtener repositorios.
     * Se utiliza para implementar una forma sencilla de inyección de dependencias manual.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Inicializa el contenedor de datos pasando el contexto de la aplicación
        container = AppDataContainer(this)
    }
}
