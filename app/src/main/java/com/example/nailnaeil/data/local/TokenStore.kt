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

    var refreshToken: String?
        get() = prefs.getString(KEY_REFRESH_TOKEN, null)
        set(value) = prefs.edit { putString(KEY_REFRESH_TOKEN, value) }

    var registeredDeviceToken: String?
        get() = prefs.getString(KEY_DEVICE_TOKEN, null)
        set(value) = prefs.edit { putString(KEY_DEVICE_TOKEN, value) }

    /** 관리자 샵 API(x-admin-sync-key) 인증용. 관리자 설정 화면에서 직접 입력해 저장한다. */
    var adminSyncKey: String?
        get() = prefs.getString(KEY_ADMIN_SYNC_KEY, null)
        set(value) = prefs.edit { putString(KEY_ADMIN_SYNC_KEY, value) }

    /** 관리자 디자인(매거진) API(x-admin-design-key) 인증용. */
    var adminDesignKey: String?
        get() = prefs.getString(KEY_ADMIN_DESIGN_KEY, null)
        set(value) = prefs.edit { putString(KEY_ADMIN_DESIGN_KEY, value) }

    fun clear() = prefs.edit { clear() }

    companion object {
        private const val PREFS_NAME = "nailnail_token_store"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_DEVICE_TOKEN = "registered_device_token"
        private const val KEY_ADMIN_SYNC_KEY = "admin_sync_key"
        private const val KEY_ADMIN_DESIGN_KEY = "admin_design_key"
    }
}
