package com.example.nailnaeil.ui.main.estimate

data class PriceBreakdownRow(val label: String, val amount: Int)

data class PriceBreakdownSection(
    val title: String,
    val rows: List<PriceBreakdownRow>
) {
    val totalAmount: Int get() = rows.sumOf { it.amount }
}

data class Review(val rating: String, val daysAgo: String, val content: String)

data class ShopDetail(
    val id: String,
    val shopName: String,
    val rating: String,
    val distance: String,
    val address: String,
    val closedDays: String,
    val comment: String,
    val timeSlots: List<String>,
    val styleKeywords: String,
    val priceSections: List<PriceBreakdownSection>,
    val designMatchPercent: Int,
    val reviews: List<Review>
) {
    val finalPrice: Int get() = priceSections.sumOf { it.totalAmount }
}

val MockShopDetails = listOf(
    ShopDetail(
        id = "s2",
        shopName = "유네일",
        rating = "★ 4.8 (312)",
        distance = "내 위치에서 1.2km",
        address = "서울 동작구 장승배기로10길 100",
        closedDays = "매주 화요일 휴무",
        comment = "특정 파츠가 없어서 굉장히 유사한 파츠로 대신해서 최대한 디자인해드릴게요",
        timeSlots = listOf(
            "7/4 (토)\n오후 12:00",
            "7/4 (토)\n오후 14:00",
            "7/5 (일)\n오전 11:00",
            "7/5 (일)\n오후 15:00"
        ),
        styleKeywords = "무채색톤 | 프렌치 | 열손아트",
        priceSections = listOf(
            PriceBreakdownSection("기본 가격", listOf(PriceBreakdownRow("손 젤", 35_000))),
            PriceBreakdownSection("디자인 추가", listOf(PriceBreakdownRow("아트 추가 (10)", 30_000))),
            PriceBreakdownSection(
                "옵션 추가",
                listOf(PriceBreakdownRow("파츠 추가 (2)", 10_000), PriceBreakdownRow("무광 추가", 5_000))
            )
        ),
        designMatchPercent = 87,
        reviews = listOf(
            Review(
                rating = "★★★★★ 5/5",
                daysAgo = "1일 전",
                content = "너무 좋아요. 억지로 여러 샵 탐색할 필요 없네요. 손톱 상태에 맞게 추천도 잘해주시고, 다른 디자인 갖고 가도 꼼꼼히 잘 해주세요."
            ),
            Review(
                rating = "★★★★★ 5/5",
                daysAgo = "3일 전",
                content = "시간 약속도 잘 지켜주시고 마무리가 깔끔해요. 다음에도 또 예약할게요!"
            )
        )
    )
)
