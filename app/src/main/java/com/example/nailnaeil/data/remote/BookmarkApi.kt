package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.BookmarkResponse
import com.example.nailnaeil.data.remote.dto.DesignWishResponse
import com.example.nailnaeil.data.remote.dto.ShopWishResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookmarkApi {

    @GET("api/v1/users/me/bookmark")
    suspend fun getBookmarks(
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int = 10
    ): Response<ApiResponse<BookmarkResponse>>

    @POST("api/v1/designs/{designId}/wish")
    suspend fun addDesignWish(
        @Path("designId") designId: Long
    ): Response<ApiResponse<DesignWishResponse>>

    @DELETE("api/v1/designs/{designId}/wish")
    suspend fun removeDesignWish(
        @Path("designId") designId: Long
    ): Response<ApiResponse<DesignWishResponse>>

    @POST("api/v1/shops/{shopId}/wish")
    suspend fun addShopWish(
        @Path("shopId") shopId: Long
    ): Response<ApiResponse<ShopWishResponse>>

    @DELETE("api/v1/shops/{shopId}/wish")
    suspend fun removeShopWish(
        @Path("shopId") shopId: Long
    ): Response<ApiResponse<ShopWishResponse>>
}