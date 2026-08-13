package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.LoginRequest
import com.example.nailnaeil.data.remote.dto.SignupRequest
import com.example.nailnaeil.data.remote.dto.SignupResponse
import com.example.nailnaeil.data.remote.dto.TokenRefreshRequest
import com.example.nailnaeil.data.remote.dto.TokenRefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/users/signup")
    suspend fun signup(@Body request: SignupRequest): Response<ApiResponse<SignupResponse>>

    @POST("api/v1/users/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<TokenRefreshResponse>>

    // refreshToken을 바디로 인증(Bearer 아님)
    @POST("api/v1/users/auth/logout")
    suspend fun logout(@Body request: TokenRefreshRequest): Response<Unit>

    @POST("api/v1/users/auth/refresh")
    suspend fun refreshToken(@Body request: TokenRefreshRequest): Response<ApiResponse<TokenRefreshResponse>>
}
