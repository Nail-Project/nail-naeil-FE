package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.LogoutRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface UserApi {

    /**
     * 로그아웃
     * 성공 시 204 No Content
     */
    @POST("api/v1/users/auth/logout")
    suspend fun logout(
        @Body request: LogoutRequest
    ): Response<Unit>

    /**
     * 회원 탈퇴
     * AuthInterceptor가 accessToken을 자동으로 헤더에 추가한다.
     * 성공 시 204 No Content
     */
    @DELETE("api/v1/users/me")
    suspend fun deleteAccount(): Response<Unit>
}