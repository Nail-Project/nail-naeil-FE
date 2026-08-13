package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.remote.DesignApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.BookmarkedDesignsResponse
import com.example.nailnaeil.data.remote.dto.DesignDetailResponse
import com.example.nailnaeil.data.remote.dto.DesignListResponse
import com.example.nailnaeil.data.remote.dto.DesignWishResponse

class DesignRepository(
    private val api: DesignApi
) {

    suspend fun getDesigns(
        cursor: String? = null,
        category: String? = null,
        size: Int? = null
    ): Result<DesignListResponse> {
        return apiCall { api.getDesigns(cursor, category, size) }
    }

    suspend fun getDesignDetail(designId: Long): Result<DesignDetailResponse> {
        return apiCall { api.getDesignDetail(designId) }
    }

    suspend fun getDesignWishlist(cursor: String? = null, size: Int? = null): Result<BookmarkedDesignsResponse> {
        return apiCall { api.getDesignWishlist(cursor, size) }
    }

    suspend fun addDesignWish(designId: Long): Result<DesignWishResponse> {
        return apiCall { api.addDesignWish(designId) }
    }

    suspend fun removeDesignWish(designId: Long): Result<DesignWishResponse> {
        return apiCall { api.removeDesignWish(designId) }
    }
}
