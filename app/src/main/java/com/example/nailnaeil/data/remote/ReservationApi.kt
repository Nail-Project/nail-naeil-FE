package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.CancelReservationRequest
import com.example.nailnaeil.data.remote.dto.CreateReservationRequest
import com.example.nailnaeil.data.remote.dto.CreateReservationResponse
import com.example.nailnaeil.data.remote.dto.ReservationChangeResponse
import com.example.nailnaeil.data.remote.dto.ReservationDetailResponse
import com.example.nailnaeil.data.remote.dto.ReservationListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservationApi {

    @POST("api/v1/reserve")
    suspend fun createReservation(
        @Body request: CreateReservationRequest
    ): Response<ApiResponse<CreateReservationResponse>>

    @GET("api/v1/reserve/detail")
    suspend fun getReservations(
        @Query("status") status: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<ApiResponse<ReservationListResponse>>

    @GET("api/v1/reserve/{reservationId}")
    suspend fun getReservationDetail(
        @Path("reservationId") reservationId: Long
    ): Response<ApiResponse<ReservationDetailResponse>>

    @GET("api/v1/reserve/{reservationId}/change")
    suspend fun getReservationChangeInfo(
        @Path("reservationId") reservationId: Long
    ): Response<ApiResponse<ReservationChangeResponse>>

    @HTTP(
        method = "DELETE",
        path = "api/v1/reserve/{reservationId}",
        hasBody = true
    )
    suspend fun cancelReservation(
        @Path("reservationId") reservationId: Long,
        @Body request: CancelReservationRequest
    ): Response<Unit>
}