package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.demo.DemoEngine
import com.example.nailnaeil.data.remote.ReservationApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.apiCallUnit
import com.example.nailnaeil.data.remote.dto.PageInfo
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
        if (DemoEngine.isDemoProposalId(proposalId)) {
            return DemoEngine.createReservation(proposalId, timeId)?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException("데모 예약 생성에 실패했어요."))
        }
        return apiCall { api.createReservation(ReservationRequest(proposalId, timeId)) }
    }

    suspend fun getReservations(
        status: ReservationQueryStatus,
        cursor: String? = null,
        size: Int? = null
    ): Result<ReservationListResponse> {
        val demoItems = DemoEngine.listItemsForReservationList()
            .filter { item -> if (status == ReservationQueryStatus.CONFIRMED) item.status == "CONFIRMED" else item.status != "CONFIRMED" }
        return apiCall { api.getReservations(status, cursor, size) }
            .fold(
                onSuccess = { response -> Result.success(response.copy(reservations = demoItems + response.reservations)) },
                onFailure = { e ->
                    if (demoItems.isNotEmpty()) Result.success(ReservationListResponse(demoItems, PageInfo(null, false)))
                    else Result.failure(e)
                }
            )
    }

    suspend fun getReservationDetail(
        reservationId: Long,
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<ReservationDetailResponse> {
        if (DemoEngine.isDemoReservationId(reservationId)) {
            return DemoEngine.getReservationDetail(reservationId)?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException("데모 예약을 찾을 수 없어요."))
        }
        return apiCall { api.getReservationDetail(reservationId, latitude, longitude) }
    }

    suspend fun cancelReservation(reservationId: Long, reason: String): Result<Unit> {
        if (DemoEngine.isDemoReservationId(reservationId)) {
            DemoEngine.cancelReservation(reservationId)
            return Result.success(Unit)
        }
        return apiCallUnit { api.cancelReservation(reservationId, ReservationCancelRequest(reason)) }
    }
}
