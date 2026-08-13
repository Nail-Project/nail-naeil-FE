package com.example.nailnaeil.data.remote.dto

data class NotificationResponse(
    val notificationId: Long,
    val type: NotificationType,
    val targetId: Long,
    val isRead: Boolean,
    val createdAt: String
)

data class NotificationListResponse(
    val notifications: List<NotificationResponse>,
    val pageInfo: PageInfo
)

data class NotificationReadAllResponse(
    val updatedCount: Int
)
