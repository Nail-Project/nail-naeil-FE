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
    val isNPlus: Boolean,
    // 현재 백엔드 응답에 없어 항상 null로 들어온다. 추후 필드가 추가되면 이름을 맞춰 바로 연결한다.
    val nPlusJoinedAt: String? = null
)
