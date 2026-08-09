package com.example.nailnaeil.data.remote.dto

data class EstimateResultResponse(
    val requestId: Long = 0L,
    val responses: List<EstimateProposalResponse> = emptyList()
)

data class EstimateProposalResponse(
    val id: Long = 0L,
    val shop: EstimateShopResponse = EstimateShopResponse(),
    val totalPrice: Int = 0,
    val status: String = "",
    val proposalDateTimes: List<String> = emptyList(),
    val createdAt: String = ""
)

data class EstimateShopResponse(
    val id: Long = 0L,
    val name: String = "",
    val address: String = ""
)