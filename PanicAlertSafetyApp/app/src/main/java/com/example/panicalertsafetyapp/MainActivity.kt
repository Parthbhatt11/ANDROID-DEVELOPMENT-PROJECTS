package com.example.panicalertsafetyapp // Note: This package structure assumes MainActivity is in the ui folder,
// but it should ideally be in the root package.

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.panicalertsafetyapp.ui.components.AppBottomBar // Single, correct import
import com.example.panicalertsafetyapp.ui.components.PermissionHandler
import com.example.panicalertsafetyapp.ui.navigation.AppDestinations
import com.example.panicalertsafetyapp.ui.screens.*
import com.example.panicalertsafetyapp.ui.theme.PanicAlertSafetyAppTheme
import com.example.panicalertsafetyapp.ui.viewmodel.PanicViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable edge-to-edge for a modern, full-screen look
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PanicAlertSafetyAppTheme {
                AppScreen()
            }
        }
    }
}

@Composable
fun AppScreen(viewModel: PanicViewModel = viewModel()) {

    // Define the critical permissions required for the app's functionality
    val permissions = listOf(
        // For accurate location fetching (Google Fused Location Provider)
        Manifest.permission.ACCESS_FINE_LOCATION,
        // For silent emergency texts
        Manifest.permission.SEND_SMS
    )

    // The PermissionHandler acts as a gate, ensuring permissions are granted
    // before the main content (Scaffold) is displayed.
    PermissionHandler(permissions = permissions) {

        // Navigation controller for managing screens
        val navController = rememberNavController()

        Scaffold(
            // Bottom navigation bar component
            bottomBar = { AppBottomBar(navController = navController) }
        ) { padding ->

            // NavHost defines the area where different screens will be swapped out
            NavHost(
                navController = navController,
                startDestination = AppDestinations.HOME_ROUTE,
                modifier = Modifier.padding(padding)
            ) {
                // Home Screen with the main Panic Button
                composable(AppDestinations.HOME_ROUTE) {
                    HomeScreen(viewModel = viewModel)
                }

                // Contacts CRUD screen
                composable(AppDestinations.CONTACTS_ROUTE) {
                    ContactsScreen(viewModel = viewModel)
                }

                // Locations CRUD screen
                composable(AppDestinations.LOCATIONS_ROUTE) {
                    LocationsScreen(viewModel = viewModel)
                }

                // Alert History screen
                composable(AppDestinations.ALERTS_ROUTE) {
                    AlertsScreen()
                }
            }
        }
    }
}