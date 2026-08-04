package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.DeviceTokenApi
import com.example.nailnaeil.data.remote.dto.DeviceTokenRequest
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

/**
 * FCM 기기 토큰을 서버에 등록/해제한다.
 * - 로그인 성공 시 registerCurrentDeviceToken() 호출
 * - 로그아웃 시 unregisterCurrentDeviceToken() 호출
 */
class DeviceTokenRepository(
    private val api: DeviceTokenApi,
    private val tokenStore: TokenStore
) {

    suspend fun registerCurrentDeviceToken(): Result<Unit> = runCatching {
        val fcmToken = FirebaseMessaging.getInstance().token.await()
        registerDeviceToken(fcmToken)
    }

    suspend fun registerDeviceToken(fcmToken: String) {
        api.registerDeviceToken(DeviceTokenRequest(token = fcmToken))
        tokenStore.registeredDeviceToken = fcmToken
    }

    suspend fun unregisterCurrentDeviceToken(): Result<Unit> = runCatching {
        val fcmToken = tokenStore.registeredDeviceToken ?: FirebaseMessaging.getInstance().token.await()
        api.unregisterDeviceToken(DeviceTokenRequest(token = fcmToken))
        tokenStore.registeredDeviceToken = null
    }
}
