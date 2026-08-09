package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.ReservationApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.apiCallUnit
import com.example.nailnaeil.data.remote.dto.CancelReservationRequest
import com.example.nailnaeil.data.remote.dto.CreateReservationRequest
import com.example.nailnaeil.data.remote.dto.CreateReservationResponse
import com.example.nailnaeil.data.remote.dto.ReservationChangeResponse
import com.example.nailnaeil.data.remote.dto.ReservationDetailResponse
import com.example.nailnaeil.data.remote.dto.ReservationListResponse

class ReservationRepository(
    private val reservationApi: ReservationApi,
    private val tokenStore: TokenStore
) {

    suspend fun createReservation(
        proposalId: Long,
        timeId: Long
    ): Result<CreateReservationResponse> {

        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            reservationApi.createReservation(
                CreateReservationRequest(
                    proposalId = proposalId,
                    timeId = timeId
                )
            )
        }
    }

    suspend fun getReservations(
        status: String,
        page: Int = 0,
        size: Int = 10
    ): Result<ReservationListResponse> {

        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            reservationApi.getReservations(
                status = status,
                page = page,
                size = size
            )
        }
    }

    suspend fun getReservationDetail(
        reservationId: Long
    ): Result<ReservationDetailResponse> {

        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            reservationApi.getReservationDetail(
                reservationId = reservationId
            )
        }
    }

    suspend fun getReservationChangeInfo(
        reservationId: Long
    ): Result<ReservationChangeResponse> {

        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            reservationApi.getReservationChangeInfo(
                reservationId = reservationId
            )
        }
    }

    suspend fun cancelReservation(
        reservationId: Long,
        reason: String
    ): Result<Unit> {

        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCallUnit {
            reservationApi.cancelReservation(
                reservationId = reservationId,
                request = CancelReservationRequest(
                    reason = reason
                )
            )
        }
    }

    private fun hasAccessToken(): Boolean =
        !tokenStore.authToken.isNullOrBlank()

    private fun <T> loginRequiredFailure(): Result<T> =
        Result.failure(
            IllegalStateException(
                "로그인 정보가 없습니다. 다시 로그인해 주세요."
            )
        )
}