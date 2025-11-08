package com.example.panicalertsafetyapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.panicalertsafetyapp.data.model.Location
import com.example.panicalertsafetyapp.ui.viewmodel.PanicViewModel

@Composable
fun LocationsScreen(viewModel: PanicViewModel) {
    // Collect the locations list state from the ViewModel
    val locations by viewModel.locations.collectAsState()

    // State to manage the visibility of the Add Location dialog
    var showAddDialog by remember { mutableStateOf(false) }

    // State to manage the location selected for deletion
    var locationToDelete by remember { mutableStateOf<Location?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Location")
            }
        },
        content = { padding ->
            if (locations.isEmpty()) {
                EmptyListMessage("locations", padding)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(locations) { location ->
                        LocationCard(
                            location = location,
                            onDeleteClick = { locationToDelete = location }
                        )
                    }
                }
            }
        }
    )

    // Show dialogs if necessary
    if (showAddDialog) {
        AddLocationDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, lat, lon ->
                viewModel.addLocation(name, lat, lon)
                showAddDialog = false
            }
        )
    }

    locationToDelete?.let { location ->
        DeleteLocationDialog(
            location = location,
            onConfirm = {
                viewModel.deleteLocation(location)
                locationToDelete = null
            },
            onDismiss = { locationToDelete = null }
        )
    }
}

// ------------------- UI Components -------------------

@Composable
fun LocationCard(location: Location, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lat: ${String.format("%.4f", location.lat)} | Lon: ${String.format("%.4f", location.lon)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddLocationDialog(onDismiss: () -> Unit, onSave: (String, Double, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var latText by remember { mutableStateOf("") }
    var lonText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Location") },
        text = {
            Column(horizontalAlignment = Alignment.Start) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Location Name") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = latText,
                    onValueChange = { latText = it.filter { char -> char.isDigit() || char == '.' || char == '-' } },
                    label = { Text("Latitude (e.g., 34.05)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = lonText,
                    onValueChange = { lonText = it.filter { char -> char.isDigit() || char == '.' || char == '-' } },
                    label = { Text("Longitude (e.g., -118.24)") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val lat = latText.toDoubleOrNull()
                    val lon = lonText.toDoubleOrNull()
                    if (name.isNotBlank() && lat != null && lon != null) {
                        onSave(name, lat, lon)
                    }
                }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun DeleteLocationDialog(location: Location, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete ${location.name}?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text("Delete") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}