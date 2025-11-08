package com.example.panicalertsafetyapp.data.model

import kotlinx.serialization.Serializable

/**
 * Represents a predefined emergency location.
 * @property name User-friendly name of the location (e.g., "Home", "Office").
 * @property lat Latitude (Double) for accurate coordinates.
 * @property lon Longitude (Double) for accurate coordinates.
 */
@Serializable
data class Location(
    val name: String,
    val lat: Double,
    val lon: Double
)