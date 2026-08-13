package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.UserApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.apiCallUnit
import com.example.nailnaeil.data.remote.dto.UpdateUserRequest
import com.example.nailnaeil.data.remote.dto.UserMeResponse

class UserRepository(
    private val api: UserApi,
    private val tokenStore: TokenStore
) {

    suspend fun getMe(): Result<UserMeResponse> = apiCall { api.getMe() }

    suspend fun updateMe(email: String? = null, profileImageUrl: String? = null): Result<UserMeResponse> {
        return apiCall { api.updateMe(UpdateUserRequest(email = email, profileImageUrl = profileImageUrl)) }
    }

    suspend fun withdraw(): Result<Unit> {
        return apiCallUnit { api.withdraw() }.onSuccess { tokenStore.clear() }
    }
}
