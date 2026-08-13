package com.example.nailnaeil.data.remote.dto

/** cursor 기반 페이지네이션 응답에서 공통으로 쓰이는 형태. */
data class PageInfo(
    val nextCursor: String?,
    val hasNext: Boolean
)
