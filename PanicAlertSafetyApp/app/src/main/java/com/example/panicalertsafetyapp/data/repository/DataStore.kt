package com.example.panicalertsafetyapp.data.repository

import android.content.Context
import com.example.panicalertsafetyapp.data.model.Contact
import com.example.panicalertsafetyapp.data.model.Location
import com.example.panicalertsafetyapp.domain.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.File
import java.io.IOException

class DataStore(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }

    // --- Locations ---
    suspend fun loadLocations(): List<Location> = withContext(Dispatchers.IO) {
        return@withContext readFile(Constants.LOCATIONS_FILE, listOf<Location>())
    }

    suspend fun saveLocations(locations: List<Location>) = withContext(Dispatchers.IO) {
        writeFile(Constants.LOCATIONS_FILE, locations)
    }

    // --- Contacts ---
    suspend fun loadContacts(): List<Contact> = withContext(Dispatchers.IO) {
        return@withContext readFile(Constants.CONTACTS_FILE, listOf<Contact>())
    }

    suspend fun saveContacts(contacts: List<Contact>) = withContext(Dispatchers.IO) {
        writeFile(Constants.CONTACTS_FILE, contacts.sortedByDescending { it.priority })
    }

    // --- Generic File Operations ---
    private inline fun <reified T> readFile(fileName: String, default: T): T {
        val file = File(context.filesDir, fileName)
        return try {
            if (file.exists()) {
                json.decodeFromString<T>(file.readText())
            } else {
                default
            }
        } catch (e: IOException) {
            e.printStackTrace()
            default
        } catch (e: Exception) {
            e.printStackTrace()
            default
        }
    }

    private inline fun <reified T> writeFile(fileName: String, data: T) {
        val file = File(context.filesDir, fileName)
        try {
            file.writeText(json.encodeToString(data))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}