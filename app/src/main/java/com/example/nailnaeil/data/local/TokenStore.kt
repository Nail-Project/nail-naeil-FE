package com.example.nailnaeil.data.local

import android.content.Context
import androidx.core.content.edit

/**
 * 로그인 인증 토큰 / FCM 기기 토큰을 로컬에 보관하는 최소 저장소.
 * 실제 로그인 연동 시 authToken 저장 로직만 채우면 됨.
 */
class TokenStore(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var authToken: String?
        get() = prefs.getString(KEY_AUTH_TOKEN, null)
        set(value) = prefs.edit { putString(KEY_AUTH_TOKEN, value) }

    var registeredDeviceToken: String?
        get() = prefs.getString(KEY_DEVICE_TOKEN, null)
        set(value) = prefs.edit { putString(KEY_DEVICE_TOKEN, value) }

    fun clear() = prefs.edit { clear() }

    companion object {
        private const val PREFS_NAME = "nailnail_token_store"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_DEVICE_TOKEN = "registered_device_token"
    }
}
