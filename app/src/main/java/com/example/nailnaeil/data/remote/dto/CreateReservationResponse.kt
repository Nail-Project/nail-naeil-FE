package com.example.nailnaeil.data.remote.dto

data class CreateReservationResponse(
    val reservationId: Long = 0L,
    val shopName: String = "",
    val reservedAt: String = "",
    val totalPrice: Int = 0,
    val status: String = ""
)