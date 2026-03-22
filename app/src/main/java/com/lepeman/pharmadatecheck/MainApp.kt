package com.lepeman.pharmadatecheck

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavHost

@Composable
fun MainApp(navController: NavHostController = rememberNavController()) {
    PharmaNavHost(navController = navController)
}