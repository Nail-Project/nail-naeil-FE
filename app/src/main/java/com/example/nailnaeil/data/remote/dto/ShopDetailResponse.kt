package com.example.nailnaeil.data.remote.dto

data class ShopDetailResponse(
    val shopId: Long,
    val name: String,
    val address: String,
    val addressDetail: String?,
    val districtName: String?,
    val adminDongName: String?,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String?,
    val provinceName: String?,
    val locationGuide: String?,
    val parkingInfo: String?,
    val thumbnailImageUrl: String?,
    val businessHours: Map<String, String>?,
    val closedDays: List<String>?,
    val rating: Double,
    val reviewCount: Int,
    val distanceMeters: Int?,
    val isWished: Boolean
)
