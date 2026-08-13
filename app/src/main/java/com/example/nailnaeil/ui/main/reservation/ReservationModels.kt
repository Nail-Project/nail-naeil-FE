package com.example.nailnaeil.ui.main.reservation

enum class ReservationStatus(val badgeLabel: String) {
    CONFIRMED("확정"),
    COMPLETED("시술완료"),
    CANCELLED("취소");

    companion object {
        fun fromRaw(status: String): ReservationStatus = when (status) {
            "CONFIRMED" -> CONFIRMED
            "COMPLETED" -> COMPLETED
            "CANCELLED" -> CANCELLED
            else -> CONFIRMED
        }
    }
}
