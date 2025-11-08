package com.example.panicalertsafetyapp.data.repository

import android.content.Context
import com.example.panicalertsafetyapp.domain.Constants
import com.example.panicalertsafetyapp.data.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class AlertLogEntry(
    val time: String,
    val locationName: String,
    val lat: Double,
    val lon: Double
)

class AlertRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }

    suspend fun saveAlert(location: Location) = withContext(Dispatchers.IO) {
        val timeStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
        val newEntry = AlertLogEntry(
            time = timeStr,
            locationName = location.name,
            lat = location.lat,
            lon = location.lon
        )

        val currentAlerts = loadAlerts().toMutableList()
        currentAlerts.add(0, newEntry) // Add to the top

        writeFile(Constants.ALERTS_FILE, currentAlerts)
    }

    suspend fun loadAlerts(): List<AlertLogEntry> = withContext(Dispatchers.IO) {
        return@withContext readFile(Constants.ALERTS_FILE, listOf<AlertLogEntry>())
    }

    // --- Generic File Operations (copied from DataStore for simplicity/isolation) ---
    private inline fun <reified T> readFile(fileName: String, default: T): T {
        // ... (implementation same as in DataStore.kt)
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
        // ... (implementation same as in DataStore.kt)
        val file = File(context.filesDir, fileName)
        try {
            file.writeText(json.encodeToString(data))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}