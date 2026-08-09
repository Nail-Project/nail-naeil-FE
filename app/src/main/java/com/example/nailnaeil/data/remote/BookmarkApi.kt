package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.BookmarkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookmarkApi {

    @GET("api/v1/users/me/bookmark")
    suspend fun getBookmarks(
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int = 10
    ): Response<ApiResponse<BookmarkResponse>>
}