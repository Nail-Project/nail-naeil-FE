package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.UpdateUserRequest
import com.example.nailnaeil.data.remote.dto.UserMeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {

    @GET("api/v1/users/me")
    suspend fun getMe(): Response<ApiResponse<UserMeResponse>>

    @PATCH("api/v1/users/me")
    suspend fun updateMe(@Body request: UpdateUserRequest): Response<ApiResponse<UserMeResponse>>

    @DELETE("api/v1/users/me")
    suspend fun withdraw(): Response<Unit>
}
