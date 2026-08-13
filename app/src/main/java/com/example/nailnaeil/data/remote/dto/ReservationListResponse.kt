package com.example.nailnaeil.data.remote.dto

data class ReservationListItem(
    val reservationId: Long,
    val proposalId: Long,
    val reservedAt: String,
    val status: String,
    val shopName: String,
    val shopThumbnailUrl: String?,
    val totalPrice: Int,
    val nailType: String,
    val removalType: String
)

data class ReservationListResponse(
    val reservations: List<ReservationListItem>,
    val pageInfo: PageInfo
)
