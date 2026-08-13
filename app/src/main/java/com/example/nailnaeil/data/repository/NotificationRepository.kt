package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.remote.NotificationApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.dto.NotificationListResponse
import com.example.nailnaeil.data.remote.dto.NotificationReadAllResponse
import com.example.nailnaeil.data.remote.dto.NotificationResponse
import com.example.nailnaeil.data.remote.dto.NotificationSettingsRequest
import com.example.nailnaeil.data.remote.dto.NotificationSettingsResponse

class NotificationRepository(
    private val api: NotificationApi
) {

    suspend fun getNotifications(
        unread: Boolean? = null,
        cursor: String? = null,
        size: Int? = null
    ): Result<NotificationListResponse> {
        return apiCall { api.getNotifications(unread, cursor, size) }
    }

    suspend fun markNotificationRead(notificationId: Long): Result<NotificationResponse> {
        return apiCall { api.markNotificationRead(notificationId) }
    }

    suspend fun markAllNotificationsRead(): Result<NotificationReadAllResponse> {
        return apiCall { api.markAllNotificationsRead() }
    }

    suspend fun getNotificationSettings(): Result<NotificationSettingsResponse> {
        return apiCall { api.getNotificationSettings() }
    }

    suspend fun updateNotificationSettings(
        estimateEnabled: Boolean? = null,
        reservationEnabled: Boolean? = null,
        marketingEnabled: Boolean? = null
    ): Result<NotificationSettingsResponse> {
        return apiCall {
            api.updateNotificationSettings(
                NotificationSettingsRequest(estimateEnabled, reservationEnabled, marketingEnabled)
            )
        }
    }
}
