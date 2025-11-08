package com.example.panicalertsafetyapp.ui.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.*

/**
 * A reusable Composable to handle runtime permission checks and requests.
 * It gates the main application content until essential permissions are granted.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandler(permissions: List<String>, content: @Composable () -> Unit) {

    // Group all necessary permissions (e.g., ACCESS_FINE_LOCATION, SEND_SMS)
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)
    val context = LocalContext.current
    val activity = context as? Activity

    // 1. Check if all permissions are granted
    if (permissionState.allPermissionsGranted) {
        content() // Render the main application content
    } else {
        // 2. If permissions are missing, show a request screen
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "⚠️ Required Permissions",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "The Panic Alert System requires **Location** (for accurate coordinates) and **SMS** (for reliable alerts) to function in an emergency.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Check if any permission was permanently denied
            val permanentlyDenied = permissionState.permissions.any {
                !it.status.isGranted && !it.status.shouldShowRationale
            }

            if (permanentlyDenied) {
                // If denied permanently, prompt user to go to settings
                Text(
                    text = "Please enable permissions manually in your phone settings to use the app.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Intent to open the app settings page
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                            activity?.startActivity(this)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Go to App Settings")
                }
            } else {
                // Otherwise, show the standard request button
                Button(
                    onClick = { permissionState.launchMultiplePermissionRequest() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Grant Required Permissions")
                }
            }
        }
    }
}