package com.example.nailnaeil.data.remote.dto

data class NearbyShopResponse(
    val shopId: Long,
    val name: String,
    val phoneNumber: String?,
    val address: String,
    val addressDetail: String?,
    val latitude: Double,
    val longitude: Double,
    val distanceMeters: Double,
    /** CHEAP 탐색 시 견적 응답의 평균 총액. 가격 데이터가 없으면 null */
    val averagePrice: Double?
)
