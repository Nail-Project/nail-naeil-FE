package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.EstimateTimeApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.ProposalTimeResponse

class EstimateTimeRepository(
    private val estimateTimeApi: EstimateTimeApi,
    private val tokenStore: TokenStore
) {

    suspend fun getProposalTimes(
        proposalId: Long
    ): Result<ProposalTimeResponse> {

        if (tokenStore.authToken.isNullOrBlank()) {
            return Result.failure(
                IllegalStateException(
                    "로그인 정보가 없습니다. 다시 로그인해 주세요."
                )
            )
        }

        return apiCall {
            estimateTimeApi.getProposalTimes(
                proposalId = proposalId
            )
        }
    }
}