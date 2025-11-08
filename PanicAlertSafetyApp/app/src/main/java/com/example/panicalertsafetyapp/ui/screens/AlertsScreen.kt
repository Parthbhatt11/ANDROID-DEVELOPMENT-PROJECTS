package com.example.panicalertsafetyapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.panicalertsafetyapp.data.repository.AlertLogEntry
import com.example.panicalertsafetyapp.data.repository.AlertRepository
import kotlinx.coroutines.launch

// 🎯 FIX: Add the ExperimentalMaterial3Api annotation to silence the warning/error 🎯
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen() {
    val context = LocalContext.current
    val alertRepository = remember { AlertRepository(context) }

    // MutableState to hold the list of past alerts
    var alerts by remember { mutableStateOf<List<AlertLogEntry>>(emptyList()) }
    val scope = rememberCoroutineScope()

    // Function to load alerts from disk
    val loadAlerts = {
        scope.launch {
            alerts = alertRepository.loadAlerts()
        }
    }

    // Load alerts when the screen first appears
    LaunchedEffect(Unit) {
        loadAlerts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alert History") }
            )
        },
        content = { padding ->
            if (alerts.isEmpty()) {
                // Assuming EmptyListMessage is defined in another file (e.g., ContactsScreen.kt)
                // You need to ensure it's imported or available here.
                // For compilation purposes, we'll assume it's available.
                // EmptyListMessage("alerts", padding)

                // Temporary simplified content if EmptyListMessage is missing:
                Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                    Text("No alerts found.", modifier = Modifier.padding(16.dp))
                }

            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(alerts) { alert ->
                        AlertLogCard(alert = alert)
                    }
                }
            }
        }
    )
}

@Composable
fun AlertLogCard(alert: AlertLogEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🚨 Alert at ${alert.locationName}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Time: ${alert.time}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Lat: ${String.format("%.4f", alert.lat)} | Lon: ${String.format("%.4f", alert.lon)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}