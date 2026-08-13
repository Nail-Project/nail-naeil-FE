package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.remote.ShopApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.NearbyShopResponse
import com.example.nailnaeil.data.remote.dto.RecommendType
import com.example.nailnaeil.data.remote.dto.ShopBookmarkToggleRequest
import com.example.nailnaeil.data.remote.dto.ShopDetailResponse
import com.example.nailnaeil.data.remote.dto.ShopListResponse
import com.example.nailnaeil.data.remote.dto.ShopWishResponse

class ShopRepository(
    private val api: ShopApi
) {

    suspend fun getShops(
        cursor: Long? = null,
        limit: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<ShopListResponse> {
        return apiCall { api.getShops(cursor, limit, latitude, longitude) }
    }

    suspend fun searchShops(
        keyword: String,
        cursor: Long? = null,
        limit: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<ShopListResponse> {
        return apiCall { api.searchShops(keyword, cursor, limit, latitude, longitude) }
    }

    suspend fun getShopDetail(
        shopId: Long,
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<ShopDetailResponse> {
        return apiCall { api.getShopDetail(shopId, latitude, longitude) }
    }

    suspend fun getNearbyShops(
        latitude: Double,
        longitude: Double,
        recommendType: RecommendType
    ): Result<List<NearbyShopResponse>> {
        return apiCall { api.getNearbyShops(latitude, longitude, recommendType) }
    }

    suspend fun getBookmarkedShops(
        cursor: Long? = null,
        limit: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ): Result<ShopListResponse> {
        return apiCall { api.getBookmarkedShops(cursor, limit, latitude, longitude) }
    }

    suspend fun toggleShopBookmark(shopId: Long): Result<ShopWishResponse> {
        return apiCall { api.toggleShopBookmark(ShopBookmarkToggleRequest(shopId)) }
    }
}
