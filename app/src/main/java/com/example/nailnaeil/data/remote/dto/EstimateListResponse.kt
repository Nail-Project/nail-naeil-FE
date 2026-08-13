package com.example.nailnaeil.data.remote.dto

data class EstimateLowestPriceShop(
    val shopId: Long,
    val name: String,
    val address: String
)

data class EstimateListItem(
    val estimateId: Long,
    val title: String?,
    /** 요청에 첨부된 디자인 이미지 목록 (등록 순) */
    val images: List<EstimateImage>,
    val nailType: NailType,
    val removalTypes: List<RemovalType>,
    val schedules: List<EstimateSchedule>,
    val createdAt: String,
    /** MATCHING, COMPLETED, EXPIRED */
    val status: String,
    val proposalCount: Int,
    val submittedShopCount: Int,
    val minPrice: Int?,
    /** 최저가 제안 샵 정보. 응답 없으면 null. */
    val lowestPriceShop: EstimateLowestPriceShop?,
    /** 샵 탐색 시 사용한 반경 (미터) */
    val radiusMeters: Int?
)

data class EstimateListResponse(
    val estimates: List<EstimateListItem>,
    val pageInfo: PageInfo
)
