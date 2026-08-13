package com.example.nailnaeil.data.remote.dto

data class EstimateProposalTime(
    val id: Long,
    val proposalDateTime: String,
    val isSelected: Boolean
)

data class EstimateProposalTimesResponse(
    val estimateResponseId: Long,
    val proposalTimes: List<EstimateProposalTime>
)
