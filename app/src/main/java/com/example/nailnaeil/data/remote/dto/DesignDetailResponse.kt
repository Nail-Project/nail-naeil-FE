package com.example.nailnaeil.data.remote.dto

data class DesignProposalShop(
    val shopId: Long,
    val name: String,
    val address: String,
    val imageUrl: String?
)

data class DesignProposal(
    val proposalId: Long,
    val shop: DesignProposalShop,
    val price: Int
)

data class DesignDetailResponse(
    val designId: Long,
    val title: String,
    val imageUrl: String,
    val tags: List<String>,
    val isBookmarked: Boolean,
    val viewCount: Int,
    val wishCount: Int,
    val description: String?,
    val recentProposals: List<DesignProposal>
)
