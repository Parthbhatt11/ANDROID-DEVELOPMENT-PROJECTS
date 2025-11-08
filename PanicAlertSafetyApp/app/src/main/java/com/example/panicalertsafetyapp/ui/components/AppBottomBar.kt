package com.example.panicalertsafetyapp.ui.components

import androidx.compose.material.icons.Icons
// Explicitly import all icons used from the 'filled' set:
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.panicalertsafetyapp.ui.navigation.AppDestinations

/**
 * Data class defining a single item in the bottom navigation bar.
 */
data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

/**
 * Composable function to create the app's bottom navigation bar.
 */
@Composable
fun AppBottomBar(navController: NavController) {
    val items = listOf(
        NavItem(AppDestinations.HOME_ROUTE, Icons.Default.Warning, "Panic"),
        NavItem(AppDestinations.CONTACTS_ROUTE, Icons.Default.Group, "Contacts"),
        NavItem(AppDestinations.LOCATIONS_ROUTE, Icons.Default.LocationOn, "Locations"),
        NavItem(AppDestinations.ALERTS_ROUTE, Icons.Default.History, "Alerts")
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                // Highlight the currently selected item
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination to avoid a growing backstack
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        // Avoid creating multiple copies of the same destination when re-selecting
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}