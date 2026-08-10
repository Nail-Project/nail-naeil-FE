package com.example.nailnaeil.data.remote.dto

/**
 * 서버 공통 응답 포맷: {"resultType": "SUCCESS"|"FAIL", "error": {...}|null, "success": {...}|null}
 * 문서(docs/api_v2) 기준이며, 일부 엔드포인트는 아직 이 포맷이 확정되지 않았으니
 * 새 API 연동 시 실제 응답을 먼저 확인할 것.
 */
data class ApiResponse<T>(
    val resultType: String? = null,
    val error: ApiError? = null,
    val success: T? = null
)

data class ApiError(
    val code: String? = null,
    val message: String? = null,
    val data: Any? = null
)
