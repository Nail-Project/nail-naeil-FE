package com.example.nailnail.ui.main.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * 백엔드 연동 전까지 메인 화면의 견적 진행 상태를 화면 간에 공유하기 위한 mock 인메모리 상태.
 * 무료 요금제는 진행 중인 견적 1개, 플러스 요금제는 최대 3개까지 허용한다는 와이어프레임 가정을 따른다.
 */
object HomeMockState {
    var isPlus by mutableStateOf(false)

    val estimates = mutableStateListOf<EstimateSummary>()

    private val templates = listOf(
        EstimateSummary("1", "무채색톤 | 프렌치 | 열손아트", respondedCount = 3, totalCount = 5, minPrice = 49_000),
        EstimateSummary("2", "레몬 실버자석 하프 | 큐빅 포인트", respondedCount = 2, totalCount = 5, minPrice = 56_000),
        EstimateSummary("3", "레몬 실버자석 하프 | 큐빅 포인트", respondedCount = 1, totalCount = 5, minPrice = 56_000),
    )

    val maxEstimateCount: Int get() = if (isPlus) 3 else 1

    fun needsUpgrade(): Boolean = !isPlus && estimates.size >= 1

    fun addMockEstimate() {
        if (estimates.size < maxEstimateCount && estimates.size < templates.size) {
            estimates.add(templates[estimates.size])
        }
    }

    fun upgradeToPlus() {
        isPlus = true
    }
}
