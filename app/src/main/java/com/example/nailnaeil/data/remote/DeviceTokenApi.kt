package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.DeviceTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

interface DeviceTokenApi {

    @POST("api/v1/device-tokens")
    suspend fun registerDeviceToken(@Body request: DeviceTokenRequest): Response<Unit>

    // Retrofit의 @DELETE는 기본적으로 body를 지원하지 않아 @HTTP(hasBody = true)로 대체
    @HTTP(method = "DELETE", path = "api/v1/device-tokens", hasBody = true)
    suspend fun unregisterDeviceToken(@Body request: DeviceTokenRequest): Response<Unit>
}
