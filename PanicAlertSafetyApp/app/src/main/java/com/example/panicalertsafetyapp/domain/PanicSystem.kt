package com.example.panicalertsafetyapp.domain

import android.content.Context
import android.util.Log
import com.example.panicalertsafetyapp.data.model.Contact
import com.example.panicalertsafetyapp.data.model.Location
import com.example.panicalertsafetyapp.data.repository.AlertRepository
import com.example.panicalertsafetyapp.data.repository.DataStore
import com.example.panicalertsafetyapp.service.location.FusedLocationProvider
import com.example.panicalertsafetyapp.service.sms.SmsAlertManager
import com.example.panicalertsafetyapp.service.whatsapp.WhatsAppAlertManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The core business logic class that manages data, coordinates services,
 * and handles the panic alert sequence.
 */
class PanicSystem(private val context: Context) {

    // Dependency Initialization
    private val dataStore = DataStore(context)
    private val alertRepository = AlertRepository(context)
    private val locationProvider = FusedLocationProvider(context)
    private val smsManager = SmsAlertManager(context)
    private val whatsAppManager = WhatsAppAlertManager(context)

    // Coroutine Scope for background tasks
    private val scope = CoroutineScope(Dispatchers.IO)

    // Data Holders (using StateFlow to notify ViewModel/UI of changes)
    private val _locations = MutableStateFlow<List<Location>>(emptyList())
    val locations: StateFlow<List<Location>> = _locations

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    init {
        // Load data immediately upon creation
        loadAllData()
    }

    // ------------------- Data Management (CRUD) -------------------

    private fun loadAllData() = scope.launch {
        _locations.value = dataStore.loadLocations()
        _contacts.value = dataStore.loadContacts()
    }

    private fun saveContacts() = scope.launch {
        dataStore.saveContacts(_contacts.value)
    }

    private fun saveLocations() = scope.launch {
        dataStore.saveLocations(_locations.value)
    }

    // Contact CRUD
    fun addContact(name: String, phoneNumber: String, priority: Int) {
        val newContact = Contact(name, phoneNumber, priority)
        _contacts.value = (_contacts.value + newContact).sortedByDescending { it.priority }
        saveContacts()
    }

    fun deleteContact(contact: Contact) {
        _contacts.value = _contacts.value.filter { it != contact }
        saveContacts()
    }

    // Location CRUD
    fun addLocation(name: String, lat: Double, lon: Double) {
        val newLocation = Location(name, lat, lon)
        _locations.value = _locations.value + newLocation
        saveLocations()
    }

    fun deleteLocation(location: Location) {
        _locations.value = _locations.value.filter { it != location }
        saveLocations()
    }

    // ------------------- Panic Trigger Logic -------------------

    /**
     * Executes the full panic alert sequence: Location -> SMS -> WhatsApp -> Log.
     * This function should be called from the ViewModel, usually within its own CoroutineScope.
     */
    suspend fun triggerPanicSequence() {
        Log.d("PanicSystem", "Initiating panic sequence...")

        // --- 1. Get Location ---
        // Fetch the most accurate live location (suspends until a fix is found or times out)
        val liveLocation = locationProvider.getCurrentLocation()
        // Use a default/fallback if location is unavailable (prevents crash)
        val alertLocation = liveLocation ?: Location("Location Unknown", 0.0, 0.0)

        // Get sorted contacts for prioritizing alerts
        val sortedContacts = _contacts.value.sortedByDescending { it.priority }

        // --- 2. Silent & Reliable: Send SMS and Log ---
        // Run SMS and saving concurrently on a background thread
        withContext(Dispatchers.IO) {

            // a. SMS Alerts to ALL contacts
            sortedContacts.forEach { contact ->
                smsManager.sendSmsAlert(contact.phoneNumber, alertLocation)
            }

            // b. Save Alert Log
            alertRepository.saveAlert(alertLocation)
        }

        // --- 3. Interactive: WhatsApp Alert ---
        // Launch the WhatsApp Intent for the highest priority contact.
        // This must be run on the main thread because it involves launching a UI Intent.
        sortedContacts.firstOrNull()?.let { highPriorityContact ->
            withContext(Dispatchers.Main) {
                whatsAppManager.launchStaticLocationShare(highPriorityContact, alertLocation)
            }
        }

        Log.d("PanicSystem", "Panic sequence completed.")
    }
}