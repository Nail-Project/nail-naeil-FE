package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.EstimateResultApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.EstimateResultResponse

class EstimateResultRepository(
    private val estimateResultApi: EstimateResultApi,
    private val tokenStore: TokenStore
) {

    suspend fun getEstimateResult(
        requestId: Long
    ): Result<EstimateResultResponse> {

        if (tokenStore.authToken.isNullOrBlank()) {
            return Result.failure(
                IllegalStateException(
                    "로그인 정보가 없습니다. 다시 로그인해 주세요."
                )
            )
        }

        return apiCall {
            estimateResultApi.getEstimateResult(
                requestId = requestId
            )
        }
    }
}