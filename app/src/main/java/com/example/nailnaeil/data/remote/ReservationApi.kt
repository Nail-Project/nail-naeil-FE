package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.ReservationDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReservationApi {

    @GET("api/v1/reserve/{reservationId}")
    suspend fun getReservationDetail(
        @Path("reservationId")
        reservationId: Long
    ): Response<ApiResponse<ReservationDetailResponse>>
}