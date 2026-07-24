package com.example.nailnail.ui.mypage

import androidx.annotation.DrawableRes

data class FavoriteShopItem(
    val shopName: String,
    val rating: String,
    val reviewCount: String,
    val location: String,

    @DrawableRes
    val firstImageResource: Int,

    @DrawableRes
    val secondImageResource: Int,

    @DrawableRes
    val thirdImageResource: Int,

    val isPopular: Boolean = false
)