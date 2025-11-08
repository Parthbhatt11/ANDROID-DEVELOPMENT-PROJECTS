package com.example.panicalertsafetyapp.data.model

import kotlinx.serialization.Serializable

/**
 * Represents an emergency contact.
 * @property name Contact's name.
 * @property phoneNumber Contact's phone number (MUST include country code for WhatsApp/SMS).
 * @property priority Priority level (1-5, 5 being highest) for sorting/WhatsApp selection.
 */
@Serializable
data class Contact(
    val name: String,
    val phoneNumber: String, // Example: "+919876543210". Essential for SMS and WhatsApp Intent.
    val priority: Int
)