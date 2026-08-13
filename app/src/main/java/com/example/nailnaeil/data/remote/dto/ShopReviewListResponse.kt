package com.example.nailnaeil.data.remote.dto

data class ShopReviewItem(
    val reviewId: Long,
    val nickname: String?,
    val profileImageUrl: String?,
    val rating: Int,
    val content: String?,
    val createdAt: String
)

data class ShopReviewListResponse(
    val reviews: List<ShopReviewItem>,
    val nextCursor: Long?
)
