package com.example.nailnaeil.data.remote.dto

data class ReservationDetailResponse(
    val reservationId: Long = 0L,
    val shopName: String = "",
    val address: String = "",
    val reservedAt: String = "",
    val totalPrice: Int = 0,
    val status: String = "",
    val designName: String? = null
)