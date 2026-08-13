package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.NearbyShopResponse
import com.example.nailnaeil.data.remote.dto.RecommendType
import com.example.nailnaeil.data.remote.dto.ShopBookmarkToggleRequest
import com.example.nailnaeil.data.remote.dto.ShopDetailResponse
import com.example.nailnaeil.data.remote.dto.ShopListResponse
import com.example.nailnaeil.data.remote.dto.ShopWishResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopApi {

    @GET("api/v1/shops")
    suspend fun getShops(
        @Query("cursor") cursor: Long? = null,
        @Query("limit") limit: Int? = null,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): Response<ApiResponse<ShopListResponse>>

    @GET("api/v1/shops/search")
    suspend fun searchShops(
        @Query("keyword") keyword: String,
        @Query("cursor") cursor: Long? = null,
        @Query("limit") limit: Int? = null,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): Response<ApiResponse<ShopListResponse>>

    @GET("api/v1/shops/{shopId}")
    suspend fun getShopDetail(
        @Path("shopId") shopId: Long,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): Response<ApiResponse<ShopDetailResponse>>

    @GET("api/v1/shops/matches")
    suspend fun getNearbyShops(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("recommendType") recommendType: RecommendType
    ): Response<ApiResponse<List<NearbyShopResponse>>>

    /** 찜한 매장 목록 조회 (마이페이지) */
    @GET("api/v1/users/me/bookmark")
    suspend fun getBookmarkedShops(
        @Query("cursor") cursor: Long? = null,
        @Query("limit") limit: Int? = null,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): Response<ApiResponse<ShopListResponse>>

    /** 매장 찜 토글(추가/해제) - 토글 후 상태(isWished)를 반환한다. */
    @POST("api/v1/bookmark/toggle")
    suspend fun toggleShopBookmark(
        @Body request: ShopBookmarkToggleRequest
    ): Response<ApiResponse<ShopWishResponse>>
}
