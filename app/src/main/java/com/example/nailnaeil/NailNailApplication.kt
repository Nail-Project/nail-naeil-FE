package com.example.nailnaeil

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.nailnaeil.di.AppContainer

class NailNailApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContainer.init(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            getString(R.string.fcm_default_channel_id),
            "네일내일 알림",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}
