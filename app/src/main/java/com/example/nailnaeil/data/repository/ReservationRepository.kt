package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.remote.ReservationApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.apiCallUnit
import com.example.nailnaeil.data.remote.dto.ReservationCancelRequest
import com.example.nailnaeil.data.remote.dto.ReservationDetailResponse
import com.example.nailnaeil.data.remote.dto.ReservationListResponse
import com.example.nailnaeil.data.remote.dto.ReservationQueryStatus
import com.example.nailnaeil.data.remote.dto.ReservationRequest
import com.example.nailnaeil.data.remote.dto.ReservationResponse

class ReservationRepository(
    private val api: ReservationApi
) {

    suspend fun createReservation(proposalId: Long, timeId: Long): Result<ReservationResponse> {
        return apiCall { api.createReservation(ReservationRequest(proposalId, timeId)) }
    }

    suspend fun getReservations(
        status: ReservationQueryStatus,
        cursor: String? = null,
        size: Int? = null
    ): Result<ReservationListResponse> {
        return apiCall { api.getReservations(status, cursor, size) }
    }

    suspend fun getReservationDetail(
        reservationId: Long,
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<ReservationDetailResponse> {
        return apiCall { api.getReservationDetail(reservationId, latitude, longitude) }
    }

    suspend fun cancelReservation(reservationId: Long, reason: String): Result<Unit> {
        return apiCallUnit { api.cancelReservation(reservationId, ReservationCancelRequest(reason)) }
    }
}
