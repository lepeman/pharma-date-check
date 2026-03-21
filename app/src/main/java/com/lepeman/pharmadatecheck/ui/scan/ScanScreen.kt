package com.lepeman.pharmadatecheck.ui.scan

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lepeman.pharmadatecheck.R
import com.lepeman.pharmadatecheck.ui.navigation.PharmaNavigation
import java.lang.reflect.Modifier

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