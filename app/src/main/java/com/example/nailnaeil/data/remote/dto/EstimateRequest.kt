package com.example.nailnaeil.data.remote.dto

enum class NailType {
    HAND,
    PEDICURE,
    BOTH
}

enum class RemovalType {
    NONE,
    BASIC,
    PARTS,
    EXTENSION
}

enum class VisitTimeSlot {
    AM,
    PM,
    EVENING,
    ANY
}

data class EstimateSchedule(
    val date: String,
    val times: List<VisitTimeSlot>
)

data class EstimateRequest(
    val nailType: NailType,
    val removalTypes: List<RemovalType>,
    val schedules: List<EstimateSchedule>,
    val recommendType: RecommendType,
    val description: String? = null,
    /** 샵 탐색 시 사용한 반경 (미터, 선택). 예) 3000 → 근처 3km */
    val radiusMeters: Int? = null,
    val priceMin: Int? = null,
    val priceMax: Int? = null,
    val images: List<String>? = null,
    /** 견적을 보낼 샵 ID 목록 (최대 20개, 주변 샵 조회 API에서 받은 값) */
    val shopIds: List<Long>? = null
)
