package com.example.nailnaeil.ui.quote

import android.net.Uri
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.example.nailnaeil.data.remote.dto.EstimateResponse
import com.example.nailnaeil.data.remote.dto.NailType
import com.example.nailnaeil.data.remote.dto.NearbyShopResponse
import com.example.nailnaeil.data.remote.dto.RecommendType
import com.example.nailnaeil.data.remote.dto.RemovalType
import com.example.nailnaeil.data.remote.dto.VisitTimeSlot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class QuoteStep {
    PHOTO_UPLOAD,
    ANALYZING,
    TREATMENT_INFO,
    SCHEDULE,
    RANGE_SELECT,
    CONFIRM,
    SUCCESS
}

fun VisitTimeSlot.label(): String = when (this) {
    VisitTimeSlot.ANY -> "상관없음"
    VisitTimeSlot.AM -> "오전"
    VisitTimeSlot.PM -> "오후"
    VisitTimeSlot.EVENING -> "저녁"
}

enum class SearchRadius(
    val radiusMeters: Int?,
    val recommendType: RecommendType,
    val headline: String,
    val subtitle: String,
    val buttonLabel: String,
    val rangeLabel: String
) {
    NEAR(2_000, RecommendType.CLOSE, "가까운 샵 위주로 찾을게요", "빠른 예약이 필요할 때 좋습니다", "적용하기", "2km 이내"),
    MODERATE(5_000, RecommendType.BALANCED, "적절한 균형으로 찾을게요", "가장 인기있는 가성비 전략", "디자인 견적받기", "5km 이내"),
    WIDE(10_000, RecommendType.WIDE, "넓게 찾아드려요", "최저가를 위해 더 넓게 찾아드려요", "디자인 견적받기", "10km 이내"),
    CITYWIDE(null, RecommendType.CHEAP, "가장 저렴한 곳을 찾을게요", "네일내일에서 확보한 가장 저렴한 견적을 찾아드려요", "디자인 견적받기", "서울 전체")
}

data class QuoteDateOption(val date: String, val label: String)

/** 오늘부터 7일 이내 방문 가능 날짜 목록(서버 스펙: 오늘~오늘+7일 이내). */
fun upcomingDateOptions(): List<QuoteDateOption> {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val labelFormat = SimpleDateFormat("M월 d일 (E)", Locale.KOREA)
    return (0..7).map { offset ->
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, offset)
        QuoteDateOption(date = isoFormat.format(cal.time), label = labelFormat.format(cal.time))
    }
}

data class CalendarDay(val date: String, val dayOfMonth: Int, val weekdayIndex: Int, val isToday: Boolean, val selectable: Boolean)

data class CalendarMonth(val label: String, val leadingBlanks: Int, val days: List<CalendarDay>)

/**
 * 오늘~오늘+7일 범위를 달력 그리드로 보여주기 위한 월 단위 데이터.
 * 범위가 달을 넘어가면(예: 8/28~9/4) 두 달 블록을 이어서 반환한다.
 */
fun quoteCalendarMonths(): List<CalendarMonth> {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val today = Calendar.getInstance()
    val todayString = isoFormat.format(today.time)

    val selectableDates = (0..7).map { offset ->
        val cal = today.clone() as Calendar
        cal.add(Calendar.DAY_OF_MONTH, offset)
        isoFormat.format(cal.time)
    }.toSet()

    val monthKeys = linkedSetOf<Pair<Int, Int>>()
    (0..7).forEach { offset ->
        val cal = today.clone() as Calendar
        cal.add(Calendar.DAY_OF_MONTH, offset)
        monthKeys.add(cal.get(Calendar.YEAR) to cal.get(Calendar.MONTH))
    }

    return monthKeys.map { (year, month) ->
        val first = Calendar.getInstance()
        first.set(year, month, 1)
        val daysInMonth = first.getActualMaximum(Calendar.DAY_OF_MONTH)
        val leadingBlanks = first.get(Calendar.DAY_OF_WEEK) - 1

        val days = (1..daysInMonth).map { day ->
            val cal = Calendar.getInstance()
            cal.set(year, month, day)
            val dateString = isoFormat.format(cal.time)
            CalendarDay(
                date = dateString,
                dayOfMonth = day,
                weekdayIndex = (leadingBlanks + day - 1) % 7,
                isToday = dateString == todayString,
                selectable = selectableDates.contains(dateString)
            )
        }
        CalendarMonth(label = "${month + 1}월", leadingBlanks = leadingBlanks, days = days)
    }
}

/** 견적받기 플로우 전체에서 공유되는 화면 상태 */
class QuoteUiState {
    val selectedPhotoUris = mutableStateListOf<Uri>()
    val uploadedImageUrls = mutableStateListOf<String>()
    val isUploadingImages = mutableStateOf(false)
    val uploadError = mutableStateOf<String?>(null)

    val nailType = mutableStateOf(NailType.HAND)
    val removalTypes = mutableStateListOf(RemovalType.BASIC)

    val priceLower = mutableFloatStateOf(70_000f)
    val priceUpper = mutableFloatStateOf(95_000f)
    val noPricePreference = mutableStateOf(false)

    val availableDates = upcomingDateOptions()
    val selectedDates = mutableStateListOf<String>()
    val timeSlotsByDate = mutableStateMapOf<String, androidx.compose.runtime.snapshots.SnapshotStateList<VisitTimeSlot>>()

    val requestNote = mutableStateOf("")

    val neighborhood = mutableStateOf("우리 동네")
    val searchRadius = mutableStateOf(SearchRadius.WIDE)

    val latitude = mutableStateOf<Double?>(null)
    val longitude = mutableStateOf<Double?>(null)
    val locationError = mutableStateOf<String?>(null)
    val isLoadingNearbyShops = mutableStateOf(false)
    val nearbyShops = mutableStateListOf<NearbyShopResponse>()
    /** 견적을 요청할 매장으로 사용자가 직접 선택한 목록. 기본값은 nearbyShops 전체(최대 20개)다. */
    val selectedShopIds = mutableStateListOf<Long>()

    val isSubmitting = mutableStateOf(false)
    val submitError = mutableStateOf<String?>(null)
    val submittedEstimate = mutableStateOf<EstimateResponse?>(null)

    fun toggleRemoval(option: RemovalType) {
        if (option == RemovalType.NONE) {
            if (removalTypes.contains(RemovalType.NONE)) {
                removalTypes.remove(RemovalType.NONE)
            } else {
                removalTypes.clear()
                removalTypes.add(RemovalType.NONE)
            }
        } else {
            removalTypes.remove(RemovalType.NONE)
            if (removalTypes.contains(option)) removalTypes.remove(option) else removalTypes.add(option)
        }
    }

    fun toggleShop(shopId: Long) {
        if (selectedShopIds.contains(shopId)) selectedShopIds.remove(shopId) else selectedShopIds.add(shopId)
    }

    fun timeSlotsFor(date: String) = timeSlotsByDate.getOrPut(date) { mutableStateListOf(VisitTimeSlot.ANY) }

    fun toggleDate(date: String) {
        if (selectedDates.contains(date)) {
            selectedDates.remove(date)
            timeSlotsByDate.remove(date)
        } else {
            selectedDates.add(date)
            timeSlotsByDate[date] = mutableStateListOf(VisitTimeSlot.ANY)
        }
    }

    fun toggleTimeSlot(date: String, slot: VisitTimeSlot) {
        val slots = timeSlotsFor(date)
        if (slot == VisitTimeSlot.ANY) {
            slots.clear()
            slots.add(VisitTimeSlot.ANY)
            return
        }
        slots.remove(VisitTimeSlot.ANY)
        if (slots.contains(slot)) {
            slots.remove(slot)
            if (slots.isEmpty()) slots.add(VisitTimeSlot.ANY)
        } else {
            slots.add(slot)
        }
    }

    fun dateLabel(date: String): String = availableDates.find { it.date == date }?.label ?: date

    fun scheduleSummary(): String = selectedDates.sorted().joinToString(" | ") { date ->
        "${dateLabel(date)} · ${timeSlotsFor(date).joinToString(",") { it.label() }}"
    }

    fun scheduleSummaryMultiline(): String = selectedDates.sorted().joinToString("\n") { date ->
        "${dateLabel(date)} · ${timeSlotsFor(date).joinToString(",") { it.label() }}"
    }
}
