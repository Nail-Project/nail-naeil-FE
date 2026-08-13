package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.remote.ReviewApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.ReviewDeleteResponse
import com.example.nailnaeil.data.remote.dto.ReviewRequest
import com.example.nailnaeil.data.remote.dto.ReviewResponse
import com.example.nailnaeil.data.remote.dto.ReviewUpdateRequest
import com.example.nailnaeil.data.remote.dto.ReviewUpdateResponse
import com.example.nailnaeil.data.remote.dto.ShopReviewListResponse

class ReviewRepository(
    private val api: ReviewApi
) {

    suspend fun createReview(shopId: Long, rating: Int, content: String? = null): Result<ReviewResponse> {
        return apiCall { api.createReview(ReviewRequest(shopId, rating, content)) }
    }

    suspend fun updateReview(
        reviewId: Long,
        rating: Int? = null,
        content: String? = null
    ): Result<ReviewUpdateResponse> {
        return apiCall { api.updateReview(reviewId, ReviewUpdateRequest(rating, content)) }
    }

    suspend fun deleteReview(reviewId: Long): Result<ReviewDeleteResponse> {
        return apiCall { api.deleteReview(reviewId) }
    }

    suspend fun getShopReviews(
        shopId: Long,
        cursor: String? = null,
        size: Int? = null
    ): Result<ShopReviewListResponse> {
        return apiCall { api.getShopReviews(shopId, cursor, size) }
    }
}
