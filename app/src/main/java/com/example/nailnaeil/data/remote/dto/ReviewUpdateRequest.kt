package com.example.nailnaeil.data.remote.dto

/** rating, content 중 1개 이상 채워서 보낸다(본인 리뷰만 수정 가능). */
data class ReviewUpdateRequest(
    val rating: Int? = null,
    val content: String? = null
)

data class ReviewUpdateResponse(
    val reviewId: Long,
    val shopId: Long,
    val rating: Int,
    val content: String?,
    val updatedAt: String
)
