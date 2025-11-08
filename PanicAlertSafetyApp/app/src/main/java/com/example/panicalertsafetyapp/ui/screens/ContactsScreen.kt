package com.example.panicalertsafetyapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.panicalertsafetyapp.data.model.Contact
import com.example.panicalertsafetyapp.ui.components.ContactCard
import com.example.panicalertsafetyapp.ui.viewmodel.PanicViewModel

@Composable
fun ContactsScreen(viewModel: PanicViewModel) {
    // Collect the contacts list state from the ViewModel
    val contacts by viewModel.contacts.collectAsState()

    // State to manage the visibility of the Add Contact dialog
    var showAddDialog by remember { mutableStateOf(false) }

    // State to manage the contact selected for deletion
    var contactToDelete by remember { mutableStateOf<Contact?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Contact")
            }
        },
        content = { padding ->
            if (contacts.isEmpty()) {
                EmptyListMessage("contacts", padding)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(contacts) { contact ->
                        ContactCard(
                            contact = contact,
                            onDeleteClick = { contactToDelete = contact }
                        )
                    }
                }
            }
        }
    )

    // Show dialogs if necessary
    if (showAddDialog) {
        AddContactDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, number, priority ->
                viewModel.addContact(name, number, priority)
                showAddDialog = false
            }
        )
    }

    contactToDelete?.let { contact ->
        DeleteContactDialog(
            contact = contact,
            onConfirm = {
                viewModel.deleteContact(contact)
                contactToDelete = null
            },
            onDismiss = { contactToDelete = null }
        )
    }
}

// ------------------- UI Components -------------------

@Composable
fun ContactCard(contact: Contact, onDeleteClick: () -> Unit) {
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
                    text = contact.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Phone: ${contact.phoneNumber} | Priority: ${contact.priority}",
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
fun AddContactDialog(onDismiss: () -> Unit, onSave: (String, String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var priorityText by remember { mutableStateOf("5") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Contact") },
        text = {
            Column(horizontalAlignment = Alignment.Start) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Phone Number (+Country Code)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = priorityText,
                    onValueChange = { priorityText = it.filter { char -> char.isDigit() }.take(1) },
                    label = { Text("Priority (1-5)") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val priority = priorityText.toIntOrNull()?.coerceIn(1, 5) ?: 1
                    if (name.isNotBlank() && number.isNotBlank()) {
                        onSave(name, number, priority)
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
fun DeleteContactDialog(contact: Contact, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete ${contact.name}?") },
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

@Composable
fun EmptyListMessage(itemType: String, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No $itemType added yet.",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the '+' button to add your first emergency contact.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}