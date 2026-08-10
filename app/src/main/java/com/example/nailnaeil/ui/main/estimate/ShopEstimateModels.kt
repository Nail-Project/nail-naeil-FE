package com.example.nailnaeil.ui.main.estimate

data class ShopEstimate(
    val id: String,
    val shopName: String,
    val isWaiting: Boolean,
    val averageResponseTime: String? = null,
    val removalIncluded: Boolean = true,
    val rating: String? = null,
    val distance: String? = null,
    val priceText: String? = null,
    val isLowestPrice: Boolean = false,
    val comment: String? = null,
    val timeSlots: List<String> = emptyList()
)

val MockShopEstimates = listOf(
    ShopEstimate(
        id = "s1",
        shopName = "나나네일",
        isWaiting = true,
        averageResponseTime = "평균 응답시간 : 2.5시간",
        rating = "★ 4.7 (203)",
        distance = "내 위치에서 2.1km"
    ),
    ShopEstimate(
        id = "s2",
        shopName = "유네일",
        isWaiting = false,
        rating = "★ 4.8 (881)",
        distance = "내 위치에서 1.2km",
        priceText = "69,000원",
        isLowestPrice = true,
        comment = "특정 파츠가 없어서 굉장히 유사한 파츠로 대신해서 최대한 디자인해드릴게요",
        timeSlots = listOf("7/4 (토)\n오후 12:00", "7/4 (토)\n오후 14:00")
    )
)
