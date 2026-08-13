package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.remote.EstimateApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.EstimateListResponse
import com.example.nailnaeil.data.remote.dto.EstimateProposalDetailResponse
import com.example.nailnaeil.data.remote.dto.EstimateProposalTimesResponse
import com.example.nailnaeil.data.remote.dto.EstimateRequest
import com.example.nailnaeil.data.remote.dto.EstimateResponse
import com.example.nailnaeil.data.remote.dto.EstimateResultResponse
import com.example.nailnaeil.data.remote.dto.EstimateSortType
import com.example.nailnaeil.data.remote.dto.EstimateStatus

class EstimateRepository(
    private val api: EstimateApi
) {

    suspend fun createEstimate(request: EstimateRequest): Result<EstimateResponse> {
        return apiCall { api.createEstimate(request) }
    }

    suspend fun getEstimates(
        status: EstimateStatus,
        cursor: String? = null,
        size: Int? = null
    ): Result<EstimateListResponse> {
        return apiCall { api.getEstimates(status, cursor, size) }
    }

    suspend fun getEstimateResult(
        requestId: Long,
        sort: EstimateSortType? = null
    ): Result<EstimateResultResponse> {
        return apiCall { api.getEstimateResult(requestId, sort) }
    }

    suspend fun getEstimateProposalTimes(proposalId: Long): Result<EstimateProposalTimesResponse> {
        return apiCall { api.getEstimateProposalTimes(proposalId) }
    }

    suspend fun getEstimateProposalDetail(proposalId: Long): Result<EstimateProposalDetailResponse> {
        return apiCall { api.getEstimateProposalDetail(proposalId) }
    }
}
