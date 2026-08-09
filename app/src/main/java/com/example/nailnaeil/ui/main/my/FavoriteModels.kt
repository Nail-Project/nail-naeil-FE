package com.example.nailnaeil.ui.main.my

import androidx.annotation.DrawableRes
import com.example.nailnaeil.R

data class FavoriteDesignItem(
    val designId: Long,
    val imageUrl: String,
    val designName: String,
    val tags: List<String> = emptyList(),
    val viewCount: Int = 0,
    val wishCount: Int = 0
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