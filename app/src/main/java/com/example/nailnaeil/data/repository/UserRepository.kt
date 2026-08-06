package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.UserApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.apiCallUnit
import com.example.nailnaeil.data.remote.dto.LogoutRequest
import com.example.nailnaeil.data.remote.dto.UpdateUserRequest
import com.example.nailnaeil.data.remote.dto.UserProfileResponse

class UserRepository(
    private val userApi: UserApi,
    private val tokenStore: TokenStore
) {

    suspend fun getMyPage(): Result<UserProfileResponse> {
        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            userApi.getMyPage()
        }
    }

    suspend fun updateUser(
        nickname: String,
        phoneNumber: String,
        email: String
    ): Result<UserProfileResponse> {
        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            userApi.updateUser(
                request = UpdateUserRequest(
                    nickname = nickname,
                    phoneNumber = phoneNumber,
                    email = email
                )
            )
        }
    }

    suspend fun logout(): Result<Unit> {
        val refreshToken = tokenStore.refreshToken
            ?: return loginRequiredFailure()

        return apiCallUnit {
            userApi.logout(
                LogoutRequest(
                    refreshToken = refreshToken
                )
            )
        }.onSuccess {
            tokenStore.clearAuthTokens()
        }
    }

    suspend fun deleteAccount(): Result<Unit> {
        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCallUnit {
            userApi.deleteAccount()
        }.onSuccess {
            tokenStore.clearAuthTokens()
        }
    }

    private fun hasAccessToken(): Boolean {
        return !tokenStore.authToken.isNullOrBlank()
    }

    private fun <T> loginRequiredFailure(): Result<T> {
        return Result.failure(
            IllegalStateException(
                "로그인 정보가 없습니다. 다시 로그인해 주세요."
            )
        )
    }
}