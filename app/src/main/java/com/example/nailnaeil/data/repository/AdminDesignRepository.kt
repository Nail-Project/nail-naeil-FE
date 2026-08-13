package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.AdminApi
import com.example.nailnaeil.data.remote.ApiException
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.apiCallUnit
import com.example.nailnaeil.data.remote.dto.DesignAdminCreateRequest
import com.example.nailnaeil.data.remote.dto.DesignAdminResponse
import com.example.nailnaeil.data.remote.dto.DesignAdminUpdateRequest

class AdminDesignRepository(
    private val api: AdminApi,
    private val tokenStore: TokenStore
) {

    private fun requireAdminDesignKey(): Result<String> {
        val key = tokenStore.adminDesignKey
        return if (key.isNullOrBlank()) {
            Result.failure(ApiException(null, "관리자 키(x-admin-design-key)가 설정되지 않았어요. 관리자 설정에서 먼저 입력해주세요."))
        } else {
            Result.success(key)
        }
    }

    suspend fun createDesign(request: DesignAdminCreateRequest): Result<DesignAdminResponse> {
        val key = requireAdminDesignKey().getOrElse { return Result.failure(it) }
        return apiCall { api.createDesign(key, request) }
    }

    suspend fun updateDesign(designId: Long, request: DesignAdminUpdateRequest): Result<DesignAdminResponse> {
        val key = requireAdminDesignKey().getOrElse { return Result.failure(it) }
        return apiCall { api.updateDesign(key, designId, request) }
    }

    suspend fun deleteDesign(designId: Long): Result<Unit> {
        val key = requireAdminDesignKey().getOrElse { return Result.failure(it) }
        return apiCallUnit { api.deleteDesign(key, designId) }
    }
}
