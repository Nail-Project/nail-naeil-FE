package com.example.nailnaeil.data.remote.dto

/** GET /api/v1/estimate/result/{request_id} 정렬 기준. */
enum class EstimateSortType {
    RECOMMENDED,
    LOWEST_PRICE,
    NEAREST,
    EARLIEST_AVAILABLE
}

data class EstimateResultShop(
    val id: Long,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val reviewCount: Int
)

data class EstimateResultResponseItem(
    val id: Long,
    val shop: EstimateResultShop,
    /** 시술 불가 응답이면 null */
    val totalPrice: Int?,
    /** 견적 요청 당시 좌표가 없으면 null */
    val distanceMeters: Int?,
    val isLowestPrice: Boolean,
    /** 샵이 제거비가 총액에 포함됐다고 명시한 경우 true */
    val isRemovalIncluded: Boolean,
    /** 샵이 제거 금액을 별도로 명시한 경우에만 제공 */
    val removalPrice: Int?,
    /** 예상 시술 소요 시간. 샵이 명시하지 않으면 60분 */
    val estimatedDurationMinutes: Int,
    val canProvideService: Boolean,
    /** SUBMITTED, ACCEPTED, REJECTED */
    val status: String,
    val proposalDateTimes: List<String>,
    val createdAt: String
)

data class EstimateWaitingShop(
    val shopId: Long,
    val name: String,
    /** 해당 매장의 과거 평균 응답 시간. 이력이 없으면 60분 */
    val averageResponseMinutes: Int,
    /** 예상 응답 시간. 이력이 없으면 60분 */
    val expectedResponseMinutes: Int
)

data class EstimateResultResponse(
    val requestId: Long,
    val title: String?,
    val responses: List<EstimateResultResponseItem>,
    /** 아직 견적 응답을 보내지 않은 요청 대상 매장 */
    val waitingShops: List<EstimateWaitingShop>
)
