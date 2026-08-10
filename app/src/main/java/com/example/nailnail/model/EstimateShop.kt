package com.example.nailnail.model

data class EstimateShop(
    val id: Int,
    val shopName: String,
    val rating: Double,
    val reviewCount: Int,
    val distance: Double,
    val isResponded: Boolean,       // true: 견적 도착, false: 응답 대기중
    val price: Int = 0,             // 응답 대기중일 땐 안 쓰임
    val isLowestPrice: Boolean = false, // 최저가 뱃지 여부
    val shopComment: String = "",
    val timeSlots: List<String> = emptyList() // ["7/4 (토)\n오후 12:00", ...]
)