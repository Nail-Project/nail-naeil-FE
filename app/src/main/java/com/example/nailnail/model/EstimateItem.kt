package com.example.nailnail.model // 네 패키지명에 맞게 수정해 조!

data class EstimateItem(
    val status: String,        // 응답 상태
    val replyStatus: String,   // 답변 도착 현황
    val thumbnailUrl: String,  // 썸네일 이미지
    val styleKeywords: String, // 스타일 키워드
    val options: String,       // 상세 옵션
    val dateTime: String,      // 날짜
    val dateExtra: String,     // 추가 날짜
    val shopName: String,      // 최저가 샵 이름
    val shopLocation: String,  // 샵 위치
    val price: String          // 가격
)