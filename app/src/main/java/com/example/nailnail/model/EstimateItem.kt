package com.example.nailnail.model // 네 패키지명에 맞게 수정해 조!

data class EstimateItem(
    val id: String,                 // 견적 요청 ID
    val status: String,        // 응답 상태
    val replyStatus: String,   // 답변 도착 현황
    val thumbnailUrl: String,  // 썸네일 이미지
    val styleKeywords: String, // 스타일 키워드
    val options: String,       // 상세 옵션
    val dateTime: String,      // 날짜 및 시간
    val dateExtra: String,     // 추가 날짜
    val hasLowestPrice: Boolean,    // 최저가 정보 존재 여부
    val shopName: String? = null,   // 최저가 샵 이름
    val shopLocation: String? = null,// 최저가 샵 위치
    val priceText: String? = null,  // 최저가
    val isCompleted: Boolean = false // 시술 완료 여부
)