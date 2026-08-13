package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.AuthApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.apiCallUnit
import com.example.nailnaeil.data.remote.dto.LoginRequest
import com.example.nailnaeil.data.remote.dto.SignupRequest
import com.example.nailnaeil.data.remote.dto.SignupResponse
import com.example.nailnaeil.data.remote.dto.TokenRefreshRequest

/** 회원가입/로그인/로그아웃 및 accessToken 만료 시 재발급을 처리하고 TokenStore를 갱신한다. */
class AuthRepository(
    private val api: AuthApi,
    private val tokenStore: TokenStore
) {

    suspend fun signup(
        loginId: String,
        password: String,
        email: String,
        phoneNumber: String
    ): Result<SignupResponse> {
        return apiCall { api.signup(SignupRequest(loginId, password, email, phoneNumber)) }
    }

    suspend fun login(identifier: String, password: String): Result<Unit> {
        return apiCall { api.login(LoginRequest(identifier = identifier, password = password)) }
            .onSuccess { response ->
                tokenStore.authToken = response.accessToken
                tokenStore.refreshToken = response.refreshToken
            }
            .map { }
    }

    suspend fun logout(): Result<Unit> {
        val refreshToken = tokenStore.refreshToken
            ?: return Result.failure(IllegalStateException("refreshToken이 없습니다."))

        return apiCallUnit { api.logout(TokenRefreshRequest(refreshToken = refreshToken)) }
            .onSuccess { tokenStore.clear() }
    }

    suspend fun refreshToken(): Result<Unit> {
        val refreshToken = tokenStore.refreshToken
            ?: return Result.failure(IllegalStateException("refreshToken이 없습니다."))

        return apiCall { api.refreshToken(TokenRefreshRequest(refreshToken = refreshToken)) }
            .onSuccess { response ->
                tokenStore.authToken = response.accessToken
                tokenStore.refreshToken = response.refreshToken
            }
            .map { }
    }
}
