package com.example.nailnaeil.data.remote.dto

data class ReservationChangeResponse(
    val reservationId: Long = 0L,
    val shopName: String = "",
    val shopPhoneNumber: String = "",
    val address: String = "",
    val reservedAt: String = "",
    val basePrice: Int = 0,
    val removalPrice: Int = 0,
    val extraPrice: Int = 0,
    val totalPrice: Int = 0,
    val shopComment: String? = null,
    val nailType: String = "",
    val images: List<String> = emptyList(),
    val status: String = "",
    val designName: String? = null
)