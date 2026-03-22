package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavigation

object ScanDestination : PharmaNavigation {
    override val route = "scan"
    override val titleRes = R.string.app_name
}

@Composable
fun ScanScreen(
    modifier: Modifier
) {
    Text("Luis Ortega")
}