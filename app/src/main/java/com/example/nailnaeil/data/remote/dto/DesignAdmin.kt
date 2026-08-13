package com.example.nailnaeil.data.remote.dto

data class DesignAdminCreateRequest(
    val title: String,
    val imageUrl: String,
    val durationMinutes: Int,
    val difficulty: String,
    val recommendedShape: String,
    val description: String,
    val images: List<String>? = null,
    /** 태그 이름 목록. 없는 이름이면 서버에서 새로 생성된다. */
    val tags: List<String>? = null
)

/** PATCH 시 전달한 필드만 갱신된다. images/tags는 전달하면 전체 목록을 대체한다. */
data class DesignAdminUpdateRequest(
    val title: String? = null,
    val imageUrl: String? = null,
    val durationMinutes: Int? = null,
    val difficulty: String? = null,
    val recommendedShape: String? = null,
    val description: String? = null,
    val images: List<String>? = null,
    val tags: List<String>? = null
)

/** 생성/수정 응답 스키마가 명세에 없어, 요청 필드에 식별자·시각을 더한 형태로 가정한다. */
data class DesignAdminResponse(
    val designId: Long,
    val title: String,
    val imageUrl: String,
    val durationMinutes: Int,
    val difficulty: String,
    val recommendedShape: String,
    val description: String,
    val images: List<String>?,
    val tags: List<String>?,
    val createdAt: String?,
    val updatedAt: String?
)
