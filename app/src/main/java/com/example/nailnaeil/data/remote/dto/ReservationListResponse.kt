package com.example.nailnaeil.data.remote.dto

data class ReservationListResponse(
    val reservations: List<ReservationListItemResponse> = emptyList(),
    val pageInfo: ReservationPageInfoResponse = ReservationPageInfoResponse()
)

data class ReservationListItemResponse(
    val reservationId: Long = 0L,
    val proposalId: Long = 0L,
    val reservedAt: String = "",
    val status: String = "",
    val shopName: String = "",
    val shopThumbnailUrl: String? = null,
    val totalPrice: Int = 0,
    val nailType: String = "",
    val removalType: String = ""
)

data class ReservationPageInfoResponse(
    val nextCursor: String? = null,
    val hasNext: Boolean = false
)