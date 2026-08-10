package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.BookmarkApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.BookmarkResponse
import com.example.nailnaeil.data.remote.dto.DesignWishResponse
import com.example.nailnaeil.data.remote.dto.ShopWishResponse

class BookmarkRepository(
    private val bookmarkApi: BookmarkApi,
    private val tokenStore: TokenStore
) {

    suspend fun getBookmarks(
        cursor: String? = null,
        size: Int = 10
    ): Result<BookmarkResponse> {

        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            bookmarkApi.getBookmarks(
                cursor = cursor,
                size = size
            )
        }
    }

    suspend fun addDesignWish(
        designId: Long
    ): Result<DesignWishResponse> {

        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            bookmarkApi.addDesignWish(
                designId = designId
            )
        }
    }

    suspend fun removeDesignWish(
        designId: Long
    ): Result<DesignWishResponse> {

        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            bookmarkApi.removeDesignWish(
                designId = designId
            )
        }
    }

    suspend fun addShopWish(
        shopId: Long
    ): Result<ShopWishResponse> {

        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            bookmarkApi.addShopWish(
                shopId = shopId
            )
        }
    }

    suspend fun removeShopWish(
        shopId: Long
    ): Result<ShopWishResponse> {

        if (!hasAccessToken()) {
            return loginRequiredFailure()
        }

        return apiCall {
            bookmarkApi.removeShopWish(
                shopId = shopId
            )
        }
    }

    private fun hasAccessToken(): Boolean =
        !tokenStore.authToken.isNullOrBlank()

    private fun <T> loginRequiredFailure(): Result<T> =
        Result.failure(
            IllegalStateException(
                "로그인 정보가 없습니다. 다시 로그인해 주세요."
            )
        )
}