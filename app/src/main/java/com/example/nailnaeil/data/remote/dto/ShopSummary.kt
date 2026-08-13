package com.example.nailnaeil.data.remote.dto

data class ShopSummary(
    val shopId: Long,
    val name: String,
    val address: String,
    val addressDetail: String?,
    val districtName: String?,
    val adminDongName: String?,
    val latitude: Double,
    val longitude: Double,
    val thumbnailImageUrl: String?,
    val businessHours: Map<String, String>?,
    val closedDays: List<String>?,
    val rating: Double,
    val reviewCount: Int,
    val distanceMeters: Int?,
    val isWished: Boolean
)

data class ShopListResponse(
    val shops: List<ShopSummary>,
    val nextCursor: Long?
)
