package com.example.nailnaeil.data.remote.dto

/** 해당 샵에서 시술이 완료된(COMPLETED) 본인 예약이 있어야 작성할 수 있다(서버가 검증). */
data class ReviewRequest(
    val shopId: Long,
    val rating: Int,
    val content: String? = null
)

data class ReviewResponse(
    val reviewId: Long,
    val shopId: Long,
    val rating: Int,
    val content: String?,
    val createdAt: String
)
