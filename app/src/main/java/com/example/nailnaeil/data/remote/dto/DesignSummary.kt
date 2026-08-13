package com.example.nailnaeil.data.remote.dto

data class DesignSummary(
    val designId: Long,
    val title: String,
    val imageUrl: String,
    val tags: List<String>,
    val isBookmarked: Boolean,
    val viewCount: Int,
    val wishCount: Int
)

data class DesignListResponse(
    val designs: List<DesignSummary>,
    val pageInfo: PageInfo
)
