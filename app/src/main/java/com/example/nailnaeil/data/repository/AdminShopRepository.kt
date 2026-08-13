package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.AdminApi
import com.example.nailnaeil.data.remote.ApiException
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.apiCallUnit
import com.example.nailnaeil.data.remote.dto.ShopAdmin
import com.example.nailnaeil.data.remote.dto.ShopAdminInput
import com.example.nailnaeil.data.remote.dto.ShopAdminListResponse

class AdminShopRepository(
    private val api: AdminApi,
    private val tokenStore: TokenStore
) {

    private fun requireAdminSyncKey(): Result<String> {
        val key = tokenStore.adminSyncKey
        return if (key.isNullOrBlank()) {
            Result.failure(ApiException(null, "관리자 키(x-admin-sync-key)가 설정되지 않았어요. 관리자 설정에서 먼저 입력해주세요."))
        } else {
            Result.success(key)
        }
    }

    suspend fun getShops(cursor: Long? = null, limit: Int? = null, active: String? = null): Result<ShopAdminListResponse> {
        val key = requireAdminSyncKey().getOrElse { return Result.failure(it) }
        return apiCall { api.getShops(key, cursor, limit, active) }
    }

    suspend fun getShopDetail(shopId: Long): Result<ShopAdmin> {
        val key = requireAdminSyncKey().getOrElse { return Result.failure(it) }
        return apiCall { api.getShopDetail(key, shopId) }
    }

    suspend fun createShop(input: ShopAdminInput): Result<ShopAdmin> {
        val key = requireAdminSyncKey().getOrElse { return Result.failure(it) }
        return apiCall { api.createShop(key, input) }
    }

    suspend fun updateShop(shopId: Long, input: ShopAdminInput): Result<ShopAdmin> {
        val key = requireAdminSyncKey().getOrElse { return Result.failure(it) }
        return apiCall { api.updateShop(key, shopId, input) }
    }

    suspend fun deactivateShop(shopId: Long): Result<Unit> {
        val key = requireAdminSyncKey().getOrElse { return Result.failure(it) }
        return apiCallUnit { api.deactivateShop(key, shopId) }
    }
}
