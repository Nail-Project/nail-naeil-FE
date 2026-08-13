package com.example.nailnaeil.data.remote.dto

data class ShopSyncRequest(
    /** 상권업종 소분류 코드. */
    val industryCode: String,
    val pageSize: Int? = null,
    val maxPages: Int? = null
)

data class ShopAdminInput(
    val name: String,
    val phoneNumber: String? = null,
    val address: String,
    val addressDetail: String? = null,
    val provinceCode: String? = null,
    val provinceName: String? = null,
    val districtCode: String? = null,
    val districtName: String? = null,
    val adminDongCode: String? = null,
    val adminDongName: String? = null,
    val latitude: Double,
    val longitude: Double,
    val locationGuide: String? = null,
    val parkingInfo: String? = null,
    val thumbnailImageUrl: String? = null,
    val businessHours: Map<String, String>? = null,
    val closedDays: List<String>? = null
)

data class ShopAdmin(
    val shopId: Long,
    val name: String,
    val phoneNumber: String?,
    val address: String,
    val addressDetail: String?,
    val provinceCode: String?,
    val provinceName: String?,
    val districtCode: String?,
    val districtName: String?,
    val adminDongCode: String?,
    val adminDongName: String?,
    val latitude: Double,
    val longitude: Double,
    val locationGuide: String?,
    val parkingInfo: String?,
    val thumbnailImageUrl: String?,
    val businessHours: Map<String, String>?,
    val closedDays: List<String>?,
    val dataSource: String?,
    val externalStoreId: String?,
    val rating: Double,
    val reviewCount: Int,
    val isDataActive: Boolean,
    val lastSyncedAt: String?,
    val createdAt: String,
    val updatedAt: String
)

/** 관리자 샵 목록 응답 형태는 명세에 스키마가 없어, 일반 샵 목록과 동일한 커서 페이지네이션 형태로 가정한다. */
data class ShopAdminListResponse(
    val shops: List<ShopAdmin>,
    val nextCursor: Long?
)
