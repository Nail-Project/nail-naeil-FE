package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.ProposalTimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EstimateTimeApi {

    @GET("api/v1/estimate/{proposalId}/time")
    suspend fun getProposalTimes(
        @Path("proposalId") proposalId: Long
    ): Response<ApiResponse<ProposalTimeResponse>>
}