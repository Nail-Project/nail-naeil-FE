package com.example.nailnaeil.data.remote.dto

data class ReservationDetailResponse(
    val reservationId: Long,
    val shopName: String,
    val shopPhoneNumber: String?,
    val shopRating: Double,
    val shopReviewCount: Int,
    val shopClosedDays: List<String>,
    val distanceMeters: Double,
    val address: String,
    val addressDetail: String?,
    val reservedAt: String,
    val basePrice: Int,
    val extraPrice: Int,
    val optionPrice: Int,
    val discountPrice: Int,
    val totalPrice: Int,
    val memo: String?,
    val nailType: String,
    val removalType: String,
    val images: List<String>,
    val matchScore: Int,
    val matchedImageUrls: List<String>,
    val recentReviews: List<ReviewSummary>,
    val status: String,
    val designName: String?
)
