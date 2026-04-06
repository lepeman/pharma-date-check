package com.lepeman.pharmadatecheck

import android.app.Application
import com.lepeman.pharmadatecheck.data.database.container.AppContainer
import com.lepeman.pharmadatecheck.data.database.container.AppDataContainer

/**
 * Clase de aplicación principal de PharmaDateCheck.
 *
 * Extiende [Application] para inicializar el contenedor de dependencias
 * [AppDataContainer] en el momento en que el proceso de la aplicación es
 * creado por el sistema operativo, antes de que se instancie cualquier
 * actividad, servicio o receptor.
 *
 * El contenedor [container] es accedido por [AppViewModelProvider] mediante
 * la extensión [pharmaApplication] para inyectar los repositorios necesarios
 * en cada ViewModel, sin que estos tengan conocimiento directo del mecanismo
 * de almacenamiento subyacente.
 */
class DateCheckApplication : Application() {

    /**
     * Contenedor de dependencias de la aplicación.
     *
     * Inicializado en [onCreate] con una instancia de [AppDataContainer].
     * Declarado como `lateinit` porque Android no permite pasar parámetros
     * al constructor de [Application].
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}