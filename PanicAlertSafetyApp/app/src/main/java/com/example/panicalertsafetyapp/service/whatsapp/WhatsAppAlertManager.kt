package com.example.panicalertsafetyapp.service.whatsapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.panicalertsafetyapp.data.model.Location
import com.example.panicalertsafetyapp.data.model.Contact

class WhatsAppAlertManager(private val context: Context) {

    /**
     * Launches an Intent to share a static map pin via WhatsApp to a single contact.
     * The user must manually confirm and tap 'Send' inside WhatsApp.
     */
    fun launchStaticLocationShare(contact: Contact, location: Location) {

        // The standard GEO URI scheme for simple map pin sharing
        // This is often automatically rendered as a map pin in messaging apps.
        val geoUri = "geo:${location.lat},${location.lon}?q=${location.lat},${location.lon}(Panic Location)"

        // Intent to share the text (which contains the geo URI)
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, geoUri)
            setPackage("com.whatsapp") // Target WhatsApp explicitly
            type = "text/plain"
            // Use NEW_TASK flag if called from a non-Activity context (like PanicSystem)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)

                // CRITICAL: Toast to prompt the user for the next manual step
                Toast.makeText(
                    context,
                    "⚠️ Tap 'Send' in WhatsApp for ${contact.name}. For continuous tracking, manually choose 'Share Live Location'!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Could not launch WhatsApp.", Toast.LENGTH_LONG).show()
        }
    }
}