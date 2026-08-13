package com.example.nailnaeil.data.remote

import com.example.nailnaeil.data.remote.dto.ApiResponse
import com.example.nailnaeil.data.remote.dto.NotificationListResponse
import com.example.nailnaeil.data.remote.dto.NotificationReadAllResponse
import com.example.nailnaeil.data.remote.dto.NotificationResponse
import com.example.nailnaeil.data.remote.dto.NotificationSettingsRequest
import com.example.nailnaeil.data.remote.dto.NotificationSettingsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {

    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query("unread") unread: Boolean? = null,
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int? = null
    ): Response<ApiResponse<NotificationListResponse>>

    @PATCH("api/v1/notifications/{notificationId}/read")
    suspend fun markNotificationRead(
        @Path("notificationId") notificationId: Long
    ): Response<ApiResponse<NotificationResponse>>

    @PATCH("api/v1/notifications/read-all")
    suspend fun markAllNotificationsRead(): Response<ApiResponse<NotificationReadAllResponse>>

    @GET("api/v1/notifications/settings")
    suspend fun getNotificationSettings(): Response<ApiResponse<NotificationSettingsResponse>>

    @PATCH("api/v1/notifications/settings")
    suspend fun updateNotificationSettings(
        @Body request: NotificationSettingsRequest
    ): Response<ApiResponse<NotificationSettingsResponse>>
}
