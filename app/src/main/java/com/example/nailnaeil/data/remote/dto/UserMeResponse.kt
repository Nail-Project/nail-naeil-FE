package com.example.nailnaeil.data.remote.dto

data class UserMeResponse(
    val userId: Long,
    val email: String?,
    val phoneNumber: String?,
    val nickname: String?,
    val profileImageUrl: String?,
    val role: String,
    val inProgressEstimateCount: Int,
    val upcomingReservationCount: Int,
    val isNPlus: Boolean
)
