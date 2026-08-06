package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.LogoutRequest
import com.example.nailnaeil.data.remote.dto.UpdateUserRequest
import com.example.nailnaeil.data.remote.dto.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApi {

    /**
     * 마이페이지 사용자 정보 조회
     */
    @GET("api/v1/users/me")
    suspend fun getMyPage(): Response<
            ApiResponse<UserProfileResponse>
            >

    /**
     * 회원 정보 수정
     */
    @PATCH("api/v1/users/me")
    suspend fun updateUser(
        @Body request: UpdateUserRequest
    ): Response<ApiResponse<UserProfileResponse>>

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
     * 성공 시 204 No Content
     */
    @DELETE("api/v1/users/me")
    suspend fun deleteAccount(): Response<Unit>
}