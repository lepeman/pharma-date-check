package com.lepeman.pharmadatecheck.ui.navigation

/**
 * Contrato que deben implementar los objetos de destino de navegación
 * de la aplicación.
 *
 * Cada pantalla declara un objeto singleton que implementa esta interfaz,
 * definiendo su ruta única dentro del grafo de navegación y el recurso
 * de string con su título. Esto centraliza las rutas en un único lugar
 * y evita el uso de strings literales dispersos en el código de navegación.
 *
 * @property route Ruta única que identifica el destino en el grafo de
 * navegación de [PharmaNavHost].
 * @property titleRes Identificador del recurso de string con el título
 * de la pantalla, usado por la barra de navegación inferior.
 */
interface PharmaNavigation {
    val route: String
    val titleRes: Int
}