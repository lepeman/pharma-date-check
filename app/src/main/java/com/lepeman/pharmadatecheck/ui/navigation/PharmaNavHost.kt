package com.lepeman.pharmadatecheck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.lepeman.pharmadatecheck.ui.scan.ScanDestination
import com.lepeman.pharmadatecheck.ui.scan.ScanScreen

@Composable
fun PharmaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ScanDestination.route,
        modifier = modifier
    ) {
        composable(route = ScanDestination.route) {
            ScanScreen(
                modifier = Modifier
            )
        }
    }
}