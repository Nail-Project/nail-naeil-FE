package com.example.nailnaeil.data.remote.dto

data class SignupRequest(
    val loginId: String,
    val password: String,
    val email: String,
    val phoneNumber: String
)
