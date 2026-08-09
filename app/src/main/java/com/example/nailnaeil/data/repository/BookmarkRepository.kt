package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.BookmarkApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.BookmarkResponse

class BookmarkRepository(
    private val bookmarkApi: BookmarkApi,
    private val tokenStore: TokenStore
) {

    suspend fun getBookmarks(
        cursor: String? = null,
        size: Int = 10
    ): Result<BookmarkResponse> {

        if (tokenStore.authToken.isNullOrBlank()) {
            return Result.failure(
                IllegalStateException(
                    "로그인 정보가 없습니다. 다시 로그인해 주세요."
                )
            )
        }

        return apiCall {
            bookmarkApi.getBookmarks(
                cursor = cursor,
                size = size
            )
        }
    }
}