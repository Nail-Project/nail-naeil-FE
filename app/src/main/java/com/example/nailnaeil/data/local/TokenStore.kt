package com.example.nailnaeil.data.local

import android.content.Context
import androidx.core.content.edit

/**
 * 로그인 인증 토큰과 FCM 기기 토큰을 로컬에 보관한다.
 */
class TokenStore(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * 서버 API의 accessToken.
     * 기존 AuthInterceptor에서 이 값을 Authorization 헤더에 사용한다.
     */
    var authToken: String?
        get() = prefs.getString(KEY_AUTH_TOKEN, null)
        set(value) {
            prefs.edit {
                if (value == null) {
                    remove(KEY_AUTH_TOKEN)
                } else {
                    putString(KEY_AUTH_TOKEN, value)
                }
            }
        }

    /**
     * 로그아웃 및 토큰 재발급에 사용하는 refreshToken.
     */
    var refreshToken: String?
        get() = prefs.getString(KEY_REFRESH_TOKEN, null)
        set(value) {
            prefs.edit {
                if (value == null) {
                    remove(KEY_REFRESH_TOKEN)
                } else {
                    putString(KEY_REFRESH_TOKEN, value)
                }
            }
        }

    var registeredDeviceToken: String?
        get() = prefs.getString(KEY_DEVICE_TOKEN, null)
        set(value) {
            prefs.edit {
                if (value == null) {
                    remove(KEY_DEVICE_TOKEN)
                } else {
                    putString(KEY_DEVICE_TOKEN, value)
                }
            }
        }

    /**
     * 로그아웃·회원 탈퇴 시 인증 토큰만 삭제한다.
     * FCM 기기 토큰 정보는 유지한다.
     */
    fun clearAuthTokens() {
        prefs.edit {
            remove(KEY_AUTH_TOKEN)
            remove(KEY_REFRESH_TOKEN)
        }
    }

    /**
     * 저장된 모든 값을 삭제한다.
     */
    fun clear() {
        prefs.edit {
            clear()
        }
    }

    companion object {
        private const val PREFS_NAME = "nailnail_token_store"

        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_DEVICE_TOKEN = "registered_device_token"
    }
}