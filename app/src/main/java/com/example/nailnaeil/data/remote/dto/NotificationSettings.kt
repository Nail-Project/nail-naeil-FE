package com.example.nailnaeil.data.remote.dto

/** 견적/예약/마케팅 알림 각각을 ON·OFF 한다. 전달한 항목만 갱신된다(부분 수정). */
data class NotificationSettingsRequest(
    val estimateEnabled: Boolean? = null,
    val reservationEnabled: Boolean? = null,
    val marketingEnabled: Boolean? = null
)

data class NotificationSettingsResponse(
    val estimateEnabled: Boolean,
    val reservationEnabled: Boolean,
    val marketingEnabled: Boolean
)
