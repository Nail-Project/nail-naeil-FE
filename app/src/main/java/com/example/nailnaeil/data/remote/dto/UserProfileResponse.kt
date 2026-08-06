package com.example.nailnaeil.data.remote.dto

data class UserProfileResponse(
    val userId: Long = 0L,
    val email: String = "",
    val phoneNumber: String = "",
    val nickname: String = "",
    val role: String = ""
)