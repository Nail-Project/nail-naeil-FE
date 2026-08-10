package com.example.nailnaeil.data.remote.dto

data class DeviceTokenRequest(
    val token: String,
    val platform: String = "ANDROID"
)
