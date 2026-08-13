package com.example.nailnaeil.ui.quote

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat

data class SimpleLocation(val latitude: Double, val longitude: Double)

fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

/** 마지막으로 알려진 위치를 반환한다(GPS 우선, 없으면 네트워크). 위치 서비스가 꺼져있거나 아직 위치가 없으면 null. */
fun lastKnownLocation(context: Context): SimpleLocation? {
    if (!hasLocationPermission(context)) return null
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
    val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER)
    for (provider in providers) {
        if (!manager.isProviderEnabled(provider)) continue
        val location = runCatching { manager.getLastKnownLocation(provider) }.getOrNull()
        if (location != null) return SimpleLocation(location.latitude, location.longitude)
    }
    return null
}
