package com.example.nailnail.ui.mypage

import androidx.annotation.DrawableRes

data class FavoriteDesignItem(
    @DrawableRes
    val imageResource: Int,
    val designName: String,
    val isPopular: Boolean = false
)