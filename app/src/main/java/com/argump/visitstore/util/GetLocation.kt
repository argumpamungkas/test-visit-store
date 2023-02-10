package com.argump.visitstore.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.core.content.ContextCompat

class GetLocation(val context: Context): LocationListener {

    fun getLoc(): Location? {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
            return null
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isNetworkEnabled) {
            return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } else if (isGpsEnabled) {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else {
            Toast.makeText(context, "Please Enable GPS", Toast.LENGTH_SHORT).show()
        }
        return null
    }


    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }
}