package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.remote.ReservationApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.ReservationDetailResponse

class ReservationRepository(
    private val reservationApi: ReservationApi
) {

    suspend fun getReservationDetail(
        reservationId: Long
    ): Result<ReservationDetailResponse> {

        return apiCall {
            reservationApi.getReservationDetail(
                reservationId = reservationId
            )
        }
    }
}