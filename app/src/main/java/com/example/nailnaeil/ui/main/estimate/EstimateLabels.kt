package com.example.nailnaeil.ui.main.estimate

import com.example.nailnaeil.data.remote.dto.NailType
import com.example.nailnaeil.data.remote.dto.RemovalType

fun NailType.label(): String = when (this) {
    NailType.HAND -> "손 네일"
    NailType.PEDICURE -> "발 네일"
    NailType.BOTH -> "손+발 네일"
}

fun RemovalType.label(): String = when (this) {
    RemovalType.NONE -> "제거 없음"
    RemovalType.BASIC -> "젤 제거"
    RemovalType.PARTS -> "파츠 제거"
    RemovalType.EXTENSION -> "연장 제거"
}

fun List<RemovalType>.labels(): String = joinToString("/") { it.label() }

fun estimateStatusLabel(status: String, submittedShopCount: Int): String = when (status) {
    "MATCHING" -> if (submittedShopCount > 0) "응답중" else "응답예정"
    "COMPLETED" -> "매칭완료"
    "EXPIRED" -> "만료"
    else -> status
}
