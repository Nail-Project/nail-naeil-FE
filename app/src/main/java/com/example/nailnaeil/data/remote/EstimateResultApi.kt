package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.EstimateResultResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EstimateResultApi {

    @GET("api/v1/estimate/result/{requestId}")
    suspend fun getEstimateResult(
        @Path("requestId") requestId: Long
    ): Response<ApiResponse<EstimateResultResponse>>
}