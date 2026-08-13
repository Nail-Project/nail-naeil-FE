package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.EstimateListResponse
import com.example.nailnaeil.data.remote.dto.EstimateProposalDetailResponse
import com.example.nailnaeil.data.remote.dto.EstimateProposalTimesResponse
import com.example.nailnaeil.data.remote.dto.EstimateRequest
import com.example.nailnaeil.data.remote.dto.EstimateResponse
import com.example.nailnaeil.data.remote.dto.EstimateResultResponse
import com.example.nailnaeil.data.remote.dto.EstimateSortType
import com.example.nailnaeil.data.remote.dto.EstimateStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EstimateApi {

    @POST("api/v1/estimate")
    suspend fun createEstimate(@Body request: EstimateRequest): Response<ApiResponse<EstimateResponse>>

    @GET("api/v1/estimate/{status}")
    suspend fun getEstimates(
        @Path("status") status: EstimateStatus,
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int? = null
    ): Response<ApiResponse<EstimateListResponse>>

    @GET("api/v1/estimate/result/{requestId}")
    suspend fun getEstimateResult(
        @Path("requestId") requestId: Long,
        @Query("sort") sort: EstimateSortType? = null
    ): Response<ApiResponse<EstimateResultResponse>>

    @GET("api/v1/estimate/{proposalId}/time")
    suspend fun getEstimateProposalTimes(
        @Path("proposalId") proposalId: Long
    ): Response<ApiResponse<EstimateProposalTimesResponse>>

    @GET("api/v1/estimate/{proposalId}/detail")
    suspend fun getEstimateProposalDetail(
        @Path("proposalId") proposalId: Long
    ): Response<ApiResponse<EstimateProposalDetailResponse>>

    // POST api/v1/estimate/sms 는 안드로이드 릴레이 앱 전용 웹훅으로, 이 앱(사용자 앱)이 직접 호출하지 않는다.
}
