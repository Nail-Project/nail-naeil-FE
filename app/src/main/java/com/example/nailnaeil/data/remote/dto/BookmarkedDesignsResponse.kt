package com.example.nailnaeil.data.remote.dto

/** GET /api/v1/designs/wishlist (내가 찜한 디자인 목록) 응답 형태. */
data class BookmarkedDesignItem(
    val designId: Long,
    val title: String,
    val imageUrl: String,
    val tags: List<String>,
    val viewCount: Int,
    val wishCount: Int
)

data class BookmarkedDesignsResponse(
    val designs: List<BookmarkedDesignItem>,
    val pageInfo: PageInfo
)
