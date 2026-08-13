package com.example.nailnaeil.data.remote.dto

data class ReservationRequest(
    val proposalId: Long,
    val timeId: Long
)

data class ReservationResponse(
    val reservationId: Long,
    val shopName: String,
    val reservedAt: String,
    val totalPrice: Int,
    val status: String
)
