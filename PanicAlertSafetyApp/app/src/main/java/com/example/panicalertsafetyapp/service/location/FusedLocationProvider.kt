package com.example.panicalertsafetyapp.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.example.panicalertsafetyapp.data.model.Location
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FusedLocationProvider(private val context: Context) {

    // Client for Google's Fused Location services
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Location request optimized for a single, high-accuracy fix
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        .setDurationMillis(10000) // Maximum time to wait for a fix (10 seconds)
        .setMaxUpdates(1) // Only need one result
        .build()

    /**
     * Fetches a single, high-accuracy current location fix asynchronously.
     * Requires ACCESS_FINE_LOCATION permission to be granted.
     */
    @SuppressLint("MissingPermission") // Permission check handled in UI layer (PermissionHandler)
    suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // Stop updates immediately after receiving the first result
                fusedLocationClient.removeLocationUpdates(this)

                val androidLocation = locationResult.lastLocation
                if (androidLocation != null && continuation.isActive) {
                    Log.d("LocationProvider", "Accurate fix received: ${androidLocation.latitude}, ${androidLocation.longitude}")
                    // Resume coroutine with the live location
                    continuation.resume(Location(
                        name = "Live Location",
                        lat = androidLocation.latitude,
                        lon = androidLocation.longitude
                    ))
                } else if (continuation.isActive) {
                    Log.e("LocationProvider", "Failed to get accurate location fix.")
                    // Resume with null if no location is found
                    continuation.resume(null)
                }
            }
        }

        // Request the update on the main Looper
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        // Ensure the request is cancelled if the coroutine is cancelled
        continuation.invokeOnCancellation {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}