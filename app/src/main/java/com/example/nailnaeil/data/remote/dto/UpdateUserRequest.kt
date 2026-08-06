package com.example.nailnaeil.data.remote.dto

data class UpdateUserRequest(
    val nickname: String,
    val phoneNumber: String,
    val email: String
)