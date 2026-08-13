package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.ReservationCancelRequest
import com.example.nailnaeil.data.remote.dto.ReservationDetailResponse
import com.example.nailnaeil.data.remote.dto.ReservationListResponse
import com.example.nailnaeil.data.remote.dto.ReservationQueryStatus
import com.example.nailnaeil.data.remote.dto.ReservationRequest
import com.example.nailnaeil.data.remote.dto.ReservationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservationApi {

    @POST("api/v1/reserve")
    suspend fun createReservation(@Body request: ReservationRequest): Response<ApiResponse<ReservationResponse>>

    @GET("api/v1/reserve/detail")
    suspend fun getReservations(
        @Query("status") status: ReservationQueryStatus,
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int? = null
    ): Response<ApiResponse<ReservationListResponse>>

    @GET("api/v1/reserve/{reservationId}")
    suspend fun getReservationDetail(
        @Path("reservationId") reservationId: Long,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): Response<ApiResponse<ReservationDetailResponse>>

    // Retrofit의 @DELETE는 기본적으로 body를 지원하지 않아 @HTTP(hasBody = true)로 대체
    @HTTP(method = "DELETE", path = "api/v1/reserve/{reservationId}", hasBody = true)
    suspend fun cancelReservation(
        @Path("reservationId") reservationId: Long,
        @Body request: ReservationCancelRequest
    ): Response<Unit>

    // "예약 변경"은 별도 API 없이 프론트에서 샵 연락처 안내 팝업으로 처리한다(Figma 기준, 명세에 명시).
}
