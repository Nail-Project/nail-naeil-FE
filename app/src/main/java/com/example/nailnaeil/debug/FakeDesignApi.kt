package com.example.nailnaeil.debug

import com.example.nailnaeil.data.remote.DesignApi
import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.BookmarkedDesignsResponse
import com.example.nailnaeil.data.remote.dto.DesignDetailResponse
import com.example.nailnaeil.data.remote.dto.DesignListResponse
import com.example.nailnaeil.data.remote.dto.DesignProposal
import com.example.nailnaeil.data.remote.dto.DesignProposalShop
import com.example.nailnaeil.data.remote.dto.DesignSummary
import com.example.nailnaeil.data.remote.dto.DesignWishResponse
import com.example.nailnaeil.data.remote.dto.PageInfo
import retrofit2.Response

/** UI 리뷰용 임시 목데이터. 리뷰 끝나면 이 파일과 UiPreviewScreen.kt는 삭제할 것. */

fun mockDesignSummaries(excludeId: Long? = null): List<DesignSummary> {
    val titles = listOf("글리터 프렌치", "미니멀 누드톤", "체리 아트", "마블 딥라인", "베이비 핑크 립", "블랙 라인아트")
    return (1..12).map { i ->
        DesignSummary(
            designId = i.toLong(),
            title = "${titles[i % titles.size]} $i",
            imageUrl = "https://picsum.photos/seed/design$i/400/480",
            tags = listOf("#젤네일", "#프렌치"),
            isBookmarked = i % 3 == 0,
            viewCount = 100 + i * 37,
            wishCount = 10 + i * 11
        )
    }.filter { it.designId != excludeId }
}

private fun mockShop(id: Long, name: String, address: String, seed: String, rating: Double?, reviewCount: Int?) =
    DesignProposalShop(
        shopId = id,
        name = name,
        address = address,
        imageUrl = "https://picsum.photos/seed/$seed/200/200",
        rating = rating,
        reviewCount = reviewCount
    )

fun mockProposals(): List<DesignProposal> = listOf(
    DesignProposal(
        1L, mockShop(101L, "라라네일 강남점", "서울 강남구 신사동", "shop1", 4.8, 132), 55000,
        listOf("https://picsum.photos/seed/work1/300", "https://picsum.photos/seed/work2/300", "https://picsum.photos/seed/work3/300")
    ),
    DesignProposal(
        2L, mockShop(102L, "블랑네일 홍대", "서울 마포구 서교동", "shop2", 4.5, 87), 62000,
        listOf("https://picsum.photos/seed/work4/300", "https://picsum.photos/seed/work5/300")
    ),
    DesignProposal(3L, mockShop(103L, "니나네일샵", "서울 송파구 잠실동", "shop3", null, null), 48000, emptyList()),
    DesignProposal(
        4L, mockShop(104L, "청담 프리미엄 네일", "서울 강남구 청담동", "shop4", 4.9, 210), 89000,
        listOf("https://picsum.photos/seed/work6/300")
    ),
    DesignProposal(5L, mockShop(105L, "소소네일 이태원", "서울 용산구 이태원동", "shop5", 4.2, 41), 51000, emptyList())
)

fun mockDesignDetail(designId: Long): DesignDetailResponse = DesignDetailResponse(
    designId = designId,
    title = "글리터 그라데이션 프렌치 네일",
    images = listOf("https://picsum.photos/seed/detail$designId/800/800"),
    tags = listOf("#젤네일", "#프렌치", "#글리터", "#데일리", "#손톱케어"),
    isBookmarked = false,
    viewCount = 2481,
    wishCount = 356,
    description = "은은한 그라데이션 위에 글리터 포인트를 더한 데일리 프렌치 디자인이에요. 손끝이 화사해 보이면서도 과하지 않아 데일리로 활용하기 좋아요. 오프 시 젤 제거가 필요하며, 손톱 상태에 따라 시술 시간이 달라질 수 있어요.",
    recentProposals = mockProposals(),
    estimatedPriceMin = 45000,
    estimatedPriceMax = 90000,
    averageEstimatePrice = 61000,
    durationMinutes = 90,
    difficulty = "MEDIUM",
    recommendedShape = "ROUND",
    similarDesigns = mockDesignSummaries(excludeId = designId).take(6)
)

fun mockDesignDetailEmpty(designId: Long): DesignDetailResponse = DesignDetailResponse(
    designId = designId,
    title = "심플 무광 원컬러",
    images = listOf("https://picsum.photos/seed/emptydetail$designId/800/800"),
    tags = emptyList(),
    isBookmarked = false,
    viewCount = 12,
    wishCount = 0,
    description = null,
    recentProposals = emptyList(),
    similarDesigns = emptyList()
)

class FakeDesignApi(private val empty: Boolean = false) : DesignApi {
    override suspend fun getDesigns(cursor: String?, category: String?, size: Int?): Response<ApiResponse<DesignListResponse>> {
        val list = if (empty) emptyList() else mockDesignSummaries()
        return Response.success(ApiResponse(resultType = "SUCCESS", success = DesignListResponse(list, PageInfo(null, false))))
    }

    override suspend fun getDesignDetail(designId: Long): Response<ApiResponse<DesignDetailResponse>> {
        val detail = if (empty) mockDesignDetailEmpty(designId) else mockDesignDetail(designId)
        return Response.success(ApiResponse(resultType = "SUCCESS", success = detail))
    }

    override suspend fun getDesignWishlist(cursor: String?, size: Int?): Response<ApiResponse<BookmarkedDesignsResponse>> {
        throw NotImplementedError("UI 리뷰 목데이터에서는 사용하지 않음")
    }

    override suspend fun addDesignWish(designId: Long): Response<ApiResponse<DesignWishResponse>> =
        Response.success(ApiResponse(resultType = "SUCCESS", success = DesignWishResponse(true, 357)))

    override suspend fun removeDesignWish(designId: Long): Response<ApiResponse<DesignWishResponse>> =
        Response.success(ApiResponse(resultType = "SUCCESS", success = DesignWishResponse(false, 355)))
}