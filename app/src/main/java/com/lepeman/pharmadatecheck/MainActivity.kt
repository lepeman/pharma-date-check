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
 * Actividad principal y único punto de entrada de la aplicación PharmaDateCheck.
 *
 * Habilita el diseño de borde a borde ([enableEdgeToEdge]) para aprovechar
 * toda la superficie de la pantalla, incluyendo las áreas detrás de la barra
 * de estado y la barra de navegación del sistema. El color de los iconos de
 * la barra de estado se configura en [PharmaDateCheckTheme] mediante
 * [isAppearanceLightStatusBars].
 *
 * El árbol de composición se inicia con [PharmaDateCheckTheme] como raíz del
 * tema, una [Surface] que ocupa toda la pantalla como contenedor base, y
 * [MainApp] que instancia el grafo de navegación completo de la aplicación.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PharmaDateCheckTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainApp()
                }
            }
        }
    }
}