package com.example.nailnail.ui.main.my

import androidx.annotation.DrawableRes
import com.example.nailnail.R

data class FavoriteDesignItem(
    @DrawableRes val imageRes: Int,
    val designName: String,
    val isPopular: Boolean = false
)

data class FavoriteShopItem(
    val shopName: String,
    val rating: String,
    val reviewCount: String,
    val location: String,
    @DrawableRes val firstImageRes: Int,
    @DrawableRes val secondImageRes: Int,
    @DrawableRes val thirdImageRes: Int,
    val isPopular: Boolean = false
)

val MockFavoriteDesigns = listOf(
    FavoriteDesignItem(R.drawable.img_nail_01, "도트 프렌치 네일", isPopular = true),
    FavoriteDesignItem(R.drawable.img_nail_02, "민트 핑크 아트 네일"),
    FavoriteDesignItem(R.drawable.img_nail_03, "레몬 포인트 네일"),
    FavoriteDesignItem(R.drawable.img_nail_04, "프렌치 스팽글 네일"),
    FavoriteDesignItem(R.drawable.img_nail_05, "반반 아트 네일"),
    FavoriteDesignItem(R.drawable.img_nail_06, "프렌치 아트 네일"),
    FavoriteDesignItem(R.drawable.img_nail_07, "레드 도트 네일"),
    FavoriteDesignItem(R.drawable.img_nail_08, "블랙 시럽 네일", isPopular = true)
)

val MockFavoriteShops = listOf(
    FavoriteShopItem(
        shopName = "유네일",
        rating = "4.8",
        reviewCount = "(312)",
        location = "상도동",
        firstImageRes = R.drawable.nailshop1_1,
        secondImageRes = R.drawable.nailshop1_2,
        thirdImageRes = R.drawable.nailshop1_3,
        isPopular = true
    ),
    FavoriteShopItem(
        shopName = "빈벨네일",
        rating = "4.2",
        reviewCount = "(12)",
        location = "상도동",
        firstImageRes = R.drawable.nailshop2_1,
        secondImageRes = R.drawable.nailshop2_2,
        thirdImageRes = R.drawable.nailshop2_3
    ),
    FavoriteShopItem(
        shopName = "하밍네일",
        rating = "4.9",
        reviewCount = "(67)",
        location = "신대방동",
        firstImageRes = R.drawable.nailshop3_1,
        secondImageRes = R.drawable.nailshop3_2,
        thirdImageRes = R.drawable.nailshop3_3
    )
)
