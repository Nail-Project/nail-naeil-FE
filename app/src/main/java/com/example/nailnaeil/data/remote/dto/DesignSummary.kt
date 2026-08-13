package com.example.nailnaeil.data.remote.dto

data class DesignSummary(
    val designId: Long,
    val title: String,
    // 실제 응답에서 누락되는 경우가 있어 nullable. .copy() 호출 시 non-null 필드는 재검증되므로
    // 여기서 non-null로 선언하면 이 필드를 안 건드리는 .copy() 호출도 NPE로 죽는다.
    val imageUrl: String?,
    val tags: List<String>? = emptyList(),
    val isBookmarked: Boolean,
    val viewCount: Int,
    val wishCount: Int
)

data class DesignListResponse(
    val designs: List<DesignSummary>? = emptyList(),
    val pageInfo: PageInfo? = null
)
