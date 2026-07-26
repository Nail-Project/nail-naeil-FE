package com.example.nailnail.ui.main.estimate

data class EstimateItem(
    val id: String,
    val status: String,
    val replyStatus: String,
    val styleKeywords: String,
    val options: String,
    val dateTime: String,
    val dateExtra: String,
    val hasLowestPrice: Boolean,
    val shopName: String? = null,
    val shopLocation: String? = null,
    val priceText: String? = null,
    val isCompleted: Boolean = false
)

val MockEstimateItems = listOf(
    EstimateItem(
        id = "1",
        status = "응답예정",
        replyStatus = "5개 답변 예정",
        styleKeywords = "민트도트 | 크롬하츠 | 피아상골",
        options = "손 젤·젤 제거/파츠제거·근처 3km",
        dateTime = "07.04 (토) 오전",
        dateExtra = "외 2",
        hasLowestPrice = false
    ),
    EstimateItem(
        id = "2",
        status = "응답중",
        replyStatus = "5개 중 2개 답변 도착",
        styleKeywords = "무채색톤 | 프렌치 | 열손아트",
        options = "손 젤·젤 제거/파츠제거·근처 10km",
        dateTime = "07.04 (토) 오전",
        dateExtra = "외 2",
        hasLowestPrice = true,
        shopName = "유네일",
        shopLocation = "서울 동작구",
        priceText = "66,500원"
    ),
    EstimateItem(
        id = "3",
        status = "시술완료",
        replyStatus = "5개 중 5개 답변 도착",
        styleKeywords = "아이보리 베이스 | 레이스 아트",
        options = "손 젤·젤 제거·근처 8km",
        dateTime = "07.04 (토) 오전",
        dateExtra = "외 2",
        hasLowestPrice = true,
        shopName = "하루네일",
        shopLocation = "서울 동작구",
        priceText = "65,000원",
        isCompleted = true
    )
)
