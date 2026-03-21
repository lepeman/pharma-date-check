package com.lepeman.pharmadatecheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.lepeman.pharmadatecheck.ui.theme.PharmaDateCheckTheme

/**
 * Punto de entrada principal de la aplicación PharmaDateCheck.
 *
 * Esta actividad configura el entorno visual inicial, habilita el diseño de borde a borde (edge-to-edge)
 * e inicializa la interfaz de usuario utilizando Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita la visualización de borde a borde para aprovechar toda la pantalla
        enableEdgeToEdge()

        setContent {
            // Aplica el tema personalizado de la aplicación definido en la capa de UI
            PharmaDateCheckTheme {
                // Inicia la función principal de navegación y composición de la App
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainApp()
                }
            }
        }
    }
}
