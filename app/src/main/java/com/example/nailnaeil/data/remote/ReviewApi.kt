package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.ReviewDeleteResponse
import com.example.nailnaeil.data.remote.dto.ReviewRequest
import com.example.nailnaeil.data.remote.dto.ReviewResponse
import com.example.nailnaeil.data.remote.dto.ReviewUpdateRequest
import com.example.nailnaeil.data.remote.dto.ReviewUpdateResponse
import com.example.nailnaeil.data.remote.dto.ShopReviewListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApi {

    @POST("api/v1/reviews")
    suspend fun createReview(@Body request: ReviewRequest): Response<ApiResponse<ReviewResponse>>

    @PATCH("api/v1/reviews/{reviewId}")
    suspend fun updateReview(
        @Path("reviewId") reviewId: Long,
        @Body request: ReviewUpdateRequest
    ): Response<ApiResponse<ReviewUpdateResponse>>

    @DELETE("api/v1/reviews/{reviewId}")
    suspend fun deleteReview(@Path("reviewId") reviewId: Long): Response<ApiResponse<ReviewDeleteResponse>>

    @GET("api/v1/shops/{shopId}/reviews")
    suspend fun getShopReviews(
        @Path("shopId") shopId: Long,
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int? = null
    ): Response<ApiResponse<ShopReviewListResponse>>
}
