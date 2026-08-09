package com.example.nailnaeil.data.remote.dto

data class ProposalTimeResponse(
    val estimateResponseId: Long = 0L,
    val proposalTimes: List<ProposalTimeItemResponse> = emptyList()
)

data class ProposalTimeItemResponse(
    val id: Long = 0L,
    val proposalDateTime: String = "",
    val isSelected: Boolean = false
)