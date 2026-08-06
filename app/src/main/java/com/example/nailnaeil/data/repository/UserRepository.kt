package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.UserApi
import com.example.nailnaeil.data.remote.apiCallUnit
import com.example.nailnaeil.data.remote.dto.LogoutRequest

class UserRepository(
    private val userApi: UserApi,
    private val tokenStore: TokenStore
) {

    suspend fun logout(): Result<Unit> {
        val refreshToken = tokenStore.refreshToken
            ?: return Result.failure(
                IllegalStateException(
                    "로그인 정보가 없습니다. 다시 로그인해 주세요."
                )
            )

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
        val accessToken = tokenStore.authToken

        if (accessToken.isNullOrBlank()) {
            return Result.failure(
                IllegalStateException(
                    "로그인 정보가 없습니다. 다시 로그인해 주세요."
                )
            )
        }

        return apiCallUnit {
            userApi.deleteAccount()
        }.onSuccess {
            tokenStore.clearAuthTokens()
        }
    }
}