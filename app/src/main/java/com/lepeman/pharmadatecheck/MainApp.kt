package com.lepeman.pharmadatecheck

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavHost

/**
 * Composable raíz de la aplicación.
 *
 * Actúa como punto de entrada de la interfaz de usuario, instanciando el
 * [NavHostController] y delegando el grafo de navegación completo a
 * [PharmaNavHost]. Al separar la creación del controlador en este composable,
 * se facilita la inyección de un controlador personalizado en pruebas sin
 * modificar [PharmaNavHost].
 *
 * @param navController Controlador de navegación. Por defecto crea uno nuevo
 * mediante [rememberNavController].
 */
@Composable
fun MainApp(navController: NavHostController = rememberNavController()) {
    PharmaNavHost(navController = navController)
}