package com.example.panicalertsafetyapp.service.sms

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import com.example.panicalertsafetyapp.data.model.Location
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmsAlertManager(private val context: Context) {

    private val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)

    fun sendSmsAlert(contactNumber: String, location: Location) {
        val timeStr = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())

        // The reliable Google Maps URL format for coordinates
        val mapsLink = "http://maps.google.com/maps?q=${location.lat},${location.lon}"

        val message = "🚨 URGENT PANIC ALERT 🚨\n" +
                "Time: $timeStr\n" +
                "Location: Live Coordinates (${location.lat}, ${location.lon})\n" +
                "VIEW MAP: $mapsLink" // This link is clickable in most phones

        try {
            // Divide message into parts if necessary (standard practice for long SMS)
            val parts = smsManager.divideMessage(message)
            smsManager.sendMultipartTextMessage(contactNumber, null, parts, null, null)
            Log.d("SMS_ALERT", "SMS sent successfully to $contactNumber")
        } catch (e: Exception) {
            Log.e("SMS_ALERT", "SMS failed to send to $contactNumber: ${e.message}")
            // In a real app, you would save this failure to the alert log
        }
    }
}