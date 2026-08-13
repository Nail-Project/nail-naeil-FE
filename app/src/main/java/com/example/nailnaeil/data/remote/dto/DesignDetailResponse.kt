package com.example.nailnaeil.data.remote.dto

data class DesignProposalShop(
    val shopId: Long,
    val name: String,
    val address: String,
    val imageUrl: String?,
    // 백엔드 응답에 아직 없어 항상 null. 추후 필드가 추가되면 이름을 맞춰 바로 연결한다.
    val rating: Double? = null,
    val reviewCount: Int? = null
)

data class DesignProposal(
    val proposalId: Long,
    val shop: DesignProposalShop,
    val price: Int,
    // 실제 응답에서 누락되는 경우가 있어 nullable로 선언. 사용할 때는 항상 .orEmpty()로 접근할 것.
    // (data class는 .copy() 호출 시 non-null로 선언된 필드를 전부 재검증하므로, 실제로 null이 들어올 수 있는
    // 필드를 non-null로 선언해두면 그 필드를 건드리지 않는 .copy() 호출도 NPE로 죽는다.)
    val workPhotoUrls: List<String>? = emptyList()
)

data class DesignDetailResponse(
    val designId: Long,
    val title: String,
    // Swagger 확인 결과 실제 응답 필드명은 "images"(배열)이다. imageUrl 단일 문자열이 아님.
    val images: List<String>? = emptyList(),
    val tags: List<String>? = emptyList(),
    val isBookmarked: Boolean,
    val viewCount: Int,
    val wishCount: Int,
    val description: String?,
    val recentProposals: List<DesignProposal>? = emptyList(),
    // 아래 두 필드는 현재 백엔드 응답에 없어 항상 null로 들어온다. 추후 필드가 추가되면 이름을 맞춰 바로 연결한다.
    val estimatedPriceMin: Int? = null,
    val estimatedPriceMax: Int? = null,
    val averageEstimatePrice: Int? = null,
    // Swagger 확인 결과 실제 필드명/타입은 "durationMinutes"(분 단위 Int). "EASY"/"ROUND" 같은 코드값으로 내려온다.
    val durationMinutes: Int? = null,
    val difficulty: String? = null,
    val recommendedShape: String? = null,
    val similarDesigns: List<DesignSummary>? = emptyList()
) {
    val imageUrl: String? get() = images?.firstOrNull()
}

fun formatDurationMinutes(minutes: Int?): String? {
    if (minutes == null || minutes <= 0) return null
    val hours = minutes / 60
    val rest = minutes % 60
    return when {
        hours > 0 && rest > 0 -> "${hours}시간 ${rest}분"
        hours > 0 -> "${hours}시간"
        else -> "${rest}분"
    }
}

fun difficultyLabel(code: String?): String? = when (code) {
    null -> null
    "EASY" -> "쉬움"
    "MEDIUM", "NORMAL" -> "보통"
    "HARD" -> "어려움"
    else -> code
}

fun shapeLabel(code: String?): String? = when (code) {
    null -> null
    "ROUND" -> "라운드"
    "SQUARE" -> "스퀘어"
    "OVAL" -> "오벌"
    "ALMOND" -> "아몬드"
    "COFFIN", "BALLERINA" -> "코핀"
    "STILETTO" -> "스틸레토"
    else -> code
}
