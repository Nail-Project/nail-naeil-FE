package com.example.nailnaeil.data.remote.dto

data class BookmarkResponse(
    val designs: List<BookmarkDesignResponse> = emptyList(),
    val pageInfo: BookmarkPageInfoResponse = BookmarkPageInfoResponse()
)

data class BookmarkDesignResponse(
    val designId: Long = 0L,
    val title: String = "",
    val imageUrl: String = "",
    val tags: List<String> = emptyList(),
    val viewCount: Int = 0,
    val wishCount: Int = 0
)

data class BookmarkPageInfoResponse(
    val nextCursor: String? = null,
    val hasNext: Boolean = false
)