package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.demo.DemoEngine
import com.example.nailnaeil.data.demo.DemoShops
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
import com.example.nailnaeil.data.remote.dto.PageInfo

class EstimateRepository(
    private val api: EstimateApi
) {

    suspend fun createEstimate(request: EstimateRequest): Result<EstimateResponse> {
        if (request.shopIds.orEmpty().any { DemoShops.isDemoShopId(it) }) {
            return Result.success(DemoEngine.createEstimate(request))
        }
        return apiCall { api.createEstimate(request) }
    }

    suspend fun getEstimates(
        status: EstimateStatus,
        cursor: String? = null,
        size: Int? = null
    ): Result<EstimateListResponse> {
        val demoItems = DemoEngine.listItemsForEstimateList()
            .filter { status == EstimateStatus.ALL || it.status == status.name }
        return apiCall { api.getEstimates(status, cursor, size) }
            .fold(
                onSuccess = { response -> Result.success(response.copy(estimates = demoItems + response.estimates)) },
                onFailure = { e ->
                    if (demoItems.isNotEmpty()) Result.success(EstimateListResponse(demoItems, PageInfo(null, false)))
                    else Result.failure(e)
                }
            )
    }

    suspend fun getEstimateResult(
        requestId: Long,
        sort: EstimateSortType? = null
    ): Result<EstimateResultResponse> {
        if (DemoEngine.isDemoEstimateId(requestId)) {
            return DemoEngine.getEstimateResult(requestId)?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException("데모 견적을 찾을 수 없어요."))
        }
        return apiCall { api.getEstimateResult(requestId, sort) }
    }

    suspend fun getEstimateProposalTimes(proposalId: Long): Result<EstimateProposalTimesResponse> {
        if (DemoEngine.isDemoProposalId(proposalId)) {
            return DemoEngine.getProposalTimes(proposalId)?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException("데모 제안을 찾을 수 없어요."))
        }
        return apiCall { api.getEstimateProposalTimes(proposalId) }
    }

    suspend fun getEstimateProposalDetail(proposalId: Long): Result<EstimateProposalDetailResponse> {
        if (DemoEngine.isDemoProposalId(proposalId)) {
            return DemoEngine.getProposalDetail(proposalId)?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException("데모 제안을 찾을 수 없어요."))
        }
        return apiCall { api.getEstimateProposalDetail(proposalId) }
    }
}
