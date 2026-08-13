package com.example.nailnaeil.data.remote.dto

data class EstimateProposalShop(
    val id: Long,
    val name: String,
    val phoneNumber: String?,
    val address: String,
    val addressDetail: String?,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val reviewCount: Int,
    /** 요일별 영업시간. 정보가 없으면 null */
    val businessHours: Map<String, String>?,
    /** 정기 휴무일. 정보가 없으면 null */
    val closedDays: List<String>?
)

/** 시술 가능한 견적의 총액은 필수이며, 시술 불가 응답에서는 totalPrice가 null이다. */
data class EstimatePrice(
    val totalPrice: Int?,
    /** 샵이 기본 가격을 명시한 경우에만 제공 */
    val basePrice: Int?,
    /** 샵이 제거 금액을 별도로 명시한 경우에만 제공 */
    val removalPrice: Int?,
    /** 샵이 디자인(아트/그림 등) 관련 추가 금액을 명시한 경우에만 제공 */
    val designExtraPrice: Int?,
    /** 샵이 그 외 옵션(젤/파츠 등) 관련 추가 금액을 명시한 경우에만 제공 */
    val optionExtraPrice: Int?
)

data class EstimateProposalDetailResponse(
    val id: Long,
    val requestId: Long,
    val title: String?,
    val shop: EstimateProposalShop,
    val price: EstimatePrice,
    val memo: String?,
    /** 예상 시술 소요 시간. 샵이 명시하지 않으면 60분 */
    val estimatedDurationMinutes: Int,
    val canProvideService: Boolean,
    /** 샵이 제거비가 총액에 포함됐다고 명시한 경우 true */
    val isRemovalIncluded: Boolean,
    /** SUBMITTED, ACCEPTED, REJECTED */
    val status: String,
    val proposalDateTimes: List<String>,
    val createdAt: String
)
