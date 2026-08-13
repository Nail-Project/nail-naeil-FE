package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.UpdateUserRequest
import com.example.nailnaeil.data.remote.dto.UserAddress
import com.example.nailnaeil.data.remote.dto.UserAddressListResponse
import com.example.nailnaeil.data.remote.dto.UserAddressRequest
import com.example.nailnaeil.data.remote.dto.UserAddressUpdateRequest
import com.example.nailnaeil.data.remote.dto.UserMeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @GET("api/v1/users/me")
    suspend fun getMe(): Response<ApiResponse<UserMeResponse>>

    @PATCH("api/v1/users/me")
    suspend fun updateMe(@Body request: UpdateUserRequest): Response<ApiResponse<UserMeResponse>>

    @DELETE("api/v1/users/me")
    suspend fun withdraw(): Response<Unit>

    @GET("api/v1/users/me/addresses")
    suspend fun getAddresses(): Response<ApiResponse<UserAddressListResponse>>

    /** 첫 번째 주소는 isDefault 값과 관계없이 기본 주소로 등록된다. */
    @POST("api/v1/users/me/addresses")
    suspend fun addAddress(@Body request: UserAddressRequest): Response<ApiResponse<UserAddress>>

    @PATCH("api/v1/users/me/addresses/{addressId}")
    suspend fun updateAddress(
        @Path("addressId") addressId: Long,
        @Body request: UserAddressUpdateRequest
    ): Response<ApiResponse<UserAddress>>

    /** 기본 주소를 삭제하면 남은 주소 중 가장 먼저 등록한 주소가 기본 주소가 된다. */
    @DELETE("api/v1/users/me/addresses/{addressId}")
    suspend fun deleteAddress(@Path("addressId") addressId: Long): Response<Unit>
}
