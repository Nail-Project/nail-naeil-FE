package com.example.nailnaeil.data.remote.dto

data class SignupResponse(
    val userId: Long,
    val loginId: String,
    val email: String,
    val role: String
)
