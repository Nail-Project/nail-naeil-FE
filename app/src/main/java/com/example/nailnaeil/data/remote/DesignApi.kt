package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.BookmarkedDesignsResponse
import com.example.nailnaeil.data.remote.dto.DesignDetailResponse
import com.example.nailnaeil.data.remote.dto.DesignListResponse
import com.example.nailnaeil.data.remote.dto.DesignWishResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DesignApi {

    @GET("api/v1/designs")
    suspend fun getDesigns(
        @Query("cursor") cursor: String? = null,
        @Query("category") category: String? = null,
        @Query("size") size: Int? = null
    ): Response<ApiResponse<DesignListResponse>>

    @GET("api/v1/designs/{designId}")
    suspend fun getDesignDetail(@Path("designId") designId: Long): Response<ApiResponse<DesignDetailResponse>>

    @GET("api/v1/designs/wishlist")
    suspend fun getDesignWishlist(
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int? = null
    ): Response<ApiResponse<BookmarkedDesignsResponse>>

    @POST("api/v1/designs/{designId}/wish")
    suspend fun addDesignWish(@Path("designId") designId: Long): Response<ApiResponse<DesignWishResponse>>

    @DELETE("api/v1/designs/{designId}/wish")
    suspend fun removeDesignWish(@Path("designId") designId: Long): Response<ApiResponse<DesignWishResponse>>
}
