package com.example.nailnaeil.data.remote.dto

data class EstimateImage(
    val imageId: Long,
    val imageUrl: String
)

/** POST /api/v1/estimate 생성 응답. */
data class EstimateResponse(
    val estimateId: Long,
    /** 자동 생성된 견적 제목. 예) "8/3 패디 견적" */
    val title: String?,
    val nailType: NailType,
    val removalTypes: List<RemovalType>,
    val schedules: List<EstimateSchedule>,
    val recommendType: RecommendType,
    val description: String?,
    /** MATCHING, COMPLETED, EXPIRED */
    val status: String,
    val designId: Long?,
    val images: List<EstimateImage>,
    val createdAt: String,
    /** 샵에 실제로 발송된 SMS 문자 원문 (클라이언트 미리보기용) */
    val smsPreview: String?
)
