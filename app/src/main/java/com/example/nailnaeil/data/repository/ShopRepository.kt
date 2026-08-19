package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.demo.DemoEngine
import com.example.nailnaeil.data.demo.DemoShops
import com.example.nailnaeil.data.remote.ShopApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.NearbyShopResponse
import com.example.nailnaeil.data.remote.dto.RecommendType
import com.example.nailnaeil.data.remote.dto.ShopBookmarkToggleRequest
import com.example.nailnaeil.data.remote.dto.ShopDetailResponse
import com.example.nailnaeil.data.remote.dto.ShopListResponse
import com.example.nailnaeil.data.remote.dto.ShopWishResponse
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
        if (DemoEngine.demoModeEnabled.value) {
            val demoShops = DemoShops.all.map { shop ->
                NearbyShopResponse(
                    shopId = shop.id,
                    name = shop.name,
                    phoneNumber = shop.phoneNumber,
                    address = shop.address,
                    addressDetail = shop.addressDetail,
                    latitude = shop.latitude,
                    longitude = shop.longitude,
                    distanceMeters = haversineMeters(latitude, longitude, shop.latitude, shop.longitude),
                    averagePrice = shop.basePrice.toDouble()
                )
            }.sortedBy { it.distanceMeters }
            return Result.success(demoShops)
        }
        return apiCall { api.getNearbyShops(latitude, longitude, recommendType) }
    }

    private fun haversineMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusMeters = 6_371_000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusMeters * c
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
