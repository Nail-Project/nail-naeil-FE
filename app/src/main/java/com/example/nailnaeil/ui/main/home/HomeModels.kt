package com.example.nailnaeil.ui.main.home

data class EstimateSummary(
    val id: String,
    val title: String,
    val respondedCount: Int,
    val totalCount: Int,
    val minPrice: Int
)

data class MagazineItem(
    val id: String,
    val title: String,
    val category: String,
    val viewCount: Int,
    val likeCount: Int
)

val MockMagazineItems = listOf(
    MagazineItem("1", "화이트 그라데이션 네일", "심플", 9359, 312),
    MagazineItem("2", "도트 프렌치 네일", "프렌치", 7599, 901),
    MagazineItem("3", "블루 아트 네일", "아트", 3420, 55),
    MagazineItem("4", "글리터 자석 네일", "마그넷", 822, 677),
    MagazineItem("5", "바코드 자석 네일", "마그넷", 1356, 199),
    MagazineItem("6", "그린, 레드 아트 네일", "아트", 654, 30),
    MagazineItem("7", "바코드 자석 네일", "마그넷", 1356, 199),
    MagazineItem("8", "그린, 레드 아트 네일", "아트", 654, 30),
)

val MagazineCategories = listOf("전체", "인기", "심플", "마그넷", "프렌치", "파츠", "아트")
