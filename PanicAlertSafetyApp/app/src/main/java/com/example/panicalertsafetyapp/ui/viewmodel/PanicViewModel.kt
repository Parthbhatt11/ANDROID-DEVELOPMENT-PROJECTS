package com.example.panicalertsafetyapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.panicalertsafetyapp.data.model.Contact
import com.example.panicalertsafetyapp.data.model.Location
import com.example.panicalertsafetyapp.domain.PanicSystem
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PanicViewModel(application: Application) : AndroidViewModel(application) {

    // Initialize the core system logic using the application context
    private val panicSystem = PanicSystem(application.applicationContext)

    // Expose data streams from the PanicSystem to the UI
    val locations: StateFlow<List<Location>> = panicSystem.locations
    val contacts: StateFlow<List<Contact>> = panicSystem.contacts

    // NOTE: You would add a StateFlow for Alerts here once AlertRepository is integrated.

    // ------------------- UI Actions -------------------

    /**
     * Executes the full panic sequence (Location -> SMS -> WhatsApp).
     */
    fun onPanicTriggered() {
        viewModelScope.launch {
            // Check permissions before calling (Permissions handled in MainActivity/PermissionHandler)
            panicSystem.triggerPanicSequence()
        }
    }

    // ------------------- CRUD Operations -------------------

    // Contact CRUD
    fun addContact(name: String, number: String, priority: Int) = viewModelScope.launch {
        panicSystem.addContact(name, number, priority)
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch {
        panicSystem.deleteContact(contact)
    }

    // Location CRUD
    fun addLocation(name: String, lat: Double, lon: Double) = viewModelScope.launch {
        panicSystem.addLocation(name, lat, lon)
    }

    fun deleteLocation(location: Location) = viewModelScope.launch {
        panicSystem.deleteLocation(location)
    }
}