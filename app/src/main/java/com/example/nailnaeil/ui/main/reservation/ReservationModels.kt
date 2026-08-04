package com.example.nailnaeil.ui.main.reservation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.mutableStateListOf
import com.example.nailnaeil.R

enum class ReservationStatus(val badgeLabel: String) {
    CONFIRMED("D-1"),
    COMPLETED("시술완료"),
    CANCELLED("취소")
}

data class Reservation(
    val id: String,
    val status: ReservationStatus,
    val dateTime: String,
    val shopName: String,
    val design: String,
    val option: String,
    val price: Int,
    @DrawableRes val imageRes: Int,
    val rating: String = "",
    val distance: String = "",
    val address: String = "",
    val closedDays: String = "",
    val comment: String = "",
    val basePrice: Int = 0,
    val designPrice: Int = 0,
    val optionPrice: Int = 0,
    val couponDiscount: Int = 0
) {
    val finalPrice: Int get() = basePrice + designPrice + optionPrice - couponDiscount
}

object ReservationMockState {
    private val confirmedList = mutableStateListOf(
        Reservation(
            id = "r1",
            status = ReservationStatus.CONFIRMED,
            dateTime = "07.04 (토) · 오후 16:00",
            shopName = "유네일",
            design = "누드원톤 | 무광 | 열손아트 | 파츠",
            option = "손 젤·젤 제거/파츠제거",
            price = 69_000,
            imageRes = R.drawable.img_nail_1,
            rating = "★ 4.8 (312)",
            distance = "내 위치에서 1.2km",
            address = "서울 동작구 장승배기로10길 100",
            closedDays = "매주 화요일 휴무",
            comment = "특정 파츠가 없어서 굉장히 유사한 파츠로 대신해서 최대한 디자인 해드릴게요",
            basePrice = 35_000,
            designPrice = 30_000,
            optionPrice = 1_500,
            couponDiscount = 0
        )
    )

    private val pastList = mutableStateListOf(
        Reservation(
            id = "r2",
            status = ReservationStatus.COMPLETED,
            dateTime = "06.02 (화) · 오후 19:00",
            shopName = "네일플랜트",
            design = "민트프렌치 | 큐빅포인트",
            option = "손 젤·젤 제거",
            price = 72_000,
            imageRes = R.drawable.img_nail_2
        ),
        Reservation(
            id = "r3",
            status = ReservationStatus.CANCELLED,
            dateTime = "05.12 (화) · 오후 13:00",
            shopName = "하늘네일",
            design = "도트아트 | 별 아트",
            option = "손 젤·젤 제거/파츠제거",
            price = 0,
            imageRes = R.drawable.img_nail_3
        ),
        Reservation(
            id = "r4",
            status = ReservationStatus.COMPLETED,
            dateTime = "03.21 (토) · 오후 16:00",
            shopName = "루네일",
            design = "파우더 | 화이트 그라데이션",
            option = "손 젤·젤 제거/파츠제거",
            price = 58_000,
            imageRes = R.drawable.img_nail_4
        )
    )

    val confirmed: List<Reservation> get() = confirmedList
    val past: List<Reservation> get() = pastList

    fun findById(id: String): Reservation? = (confirmedList + pastList).find { it.id == id }

    fun cancel(id: String) {
        val target = confirmedList.find { it.id == id } ?: return
        confirmedList.remove(target)
        pastList.add(0, target.copy(status = ReservationStatus.CANCELLED))
    }

    fun addConfirmed(reservation: Reservation) {
        confirmedList.add(0, reservation)
    }
}
