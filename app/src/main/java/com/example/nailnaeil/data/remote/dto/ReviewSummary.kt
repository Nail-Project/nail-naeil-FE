package com.example.nailnaeil.data.remote.dto

/** 견적/예약 상세에서 최근 리뷰 미리보기로 함께 내려오는 축약된 리뷰 형태. */
data class ReviewSummary(
    val rating: Int,
    val content: String,
    val createdAt: String
)
