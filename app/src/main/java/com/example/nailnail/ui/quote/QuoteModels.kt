package com.example.nailnail.ui.quote

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

enum class QuoteStep {
    PHOTO_UPLOAD,
    ANALYZING,
    TREATMENT_INFO,
    SCHEDULE,
    RANGE_SELECT,
    CONFIRM,
    SUCCESS
}

enum class TreatmentPart(val label: String) {
    HAND_GEL("손 젤"),
    FOOT_GEL("발 젤"),
    BOTH("손 + 발")
}

enum class RemovalOption(val label: String) {
    NONE("제거 없음"),
    GEL("젤 제거"),
    ART_PARTS("아트, 파츠 제거"),
    EXTENSION("연장 제거")
}

enum class TimeSlot(val label: String) {
    ANY("상관없음"),
    MORNING("오전"),
    AFTERNOON("오후"),
    EVENING("저녁")
}

enum class SearchRadius(
    val km: Int,
    val headline: String,
    val subtitle: String,
    val buttonLabel: String,
    val foundCount: Int,
    val rangeLabel: String
) {
    NEAR(2, "가까운 곳 3개 (2km 이내)", "빠른 예약이 필요할 때 좋습니다", "적용하기", 3, "2km 이내"),
    MODERATE(5, "적절한 곳 5개 (5km 이내)", "가장 인기있는 가성비 전략", "디자인 견적받기", 5, "5km 이내"),
    WIDE(10, "넓게 8개 (10km 이내)", "최저가를 위해 더 넓게 찾아드려요", "디자인 견적받기", 8, "10km 이내"),
    CITYWIDE(0, "상도동 주변 최저가 10개 (서울내)", "네일내일에서 확보한 가장 최저가를 찾아드려요", "디자인 견적받기", 10, "서울 전체")
}

data class DemoPhoto(
    val id: Int,
    val color: Color,
    val tags: List<String>
)

/** 최근 항목 갤러리를 흉내 낸 데모 이미지 목록 (실제 갤러리 연동 전 임시 데이터) */
val demoGallery: List<DemoPhoto> = run {
    val palette = listOf(
        0xFFE8CFC2, 0xFFD8B4A0, 0xFFEADCD3, 0xFFC9AE9C, 0xFFF0E0D6,
        0xFFDCC5B8, 0xFFE3D2C6, 0xFFCFB29E, 0xFFF3E6DC, 0xFFD3B8A8,
        0xFFE9D6C8, 0xFFC7A98F, 0xFFEFDDD0, 0xFFDBC0AE, 0xFFF1E2D5,
        0xFFD0AE99, 0xFFE6D0C0, 0xFFCBAA92, 0xFFEEDCCE, 0xFFDDC3B0
    )
    val tagPool = listOf(
        listOf("프렌치", "아트"), listOf("키치", "파츠"), listOf("파츠", "무채색", "유니크"),
        listOf("글리터"), listOf("오벌"), listOf("마블", "아트"), listOf("스퀘어"),
        listOf("젤리"), listOf("프렌치"), listOf("키치")
    )
    palette.mapIndexed { index, hex ->
        DemoPhoto(id = index, color = Color(hex or 0xFF000000), tags = tagPool[index % tagPool.size])
    }
}

/** 데모용 7월 달력 (오늘 = 3일, 2주 뒤인 17일까지 선택 가능) */
data class DemoDay(val day: Int, val weekdayIndex: Int, val isToday: Boolean, val selectable: Boolean)

val demoJulyDays: List<DemoDay> = (1..31).map { day ->
    val weekdayIndex = (day + 2) % 7 // 7/1 = 수요일(index 3) 기준 보정
    DemoDay(
        day = day,
        weekdayIndex = weekdayIndex,
        isToday = day == 3,
        selectable = day in 3..17
    )
}

fun weekdayLabel(index: Int): String = listOf("일", "월", "화", "수", "목", "금", "토")[index]

fun dateLabel(day: Int): String {
    val weekday = demoJulyDays.first { it.day == day }.weekdayIndex
    return "7월 ${day}일 (${weekdayLabel(weekday)})"
}

/** 견적받기 플로우 전체에서 공유되는 화면 상태 */
class QuoteUiState {
    val selectedPhotoIds = mutableStateListOf<Int>()

    val treatmentPart = mutableStateOf(TreatmentPart.HAND_GEL)
    val removalOptions = mutableStateListOf(RemovalOption.GEL)

    val priceLower = mutableFloatStateOf(70_000f)
    val priceUpper = mutableFloatStateOf(95_000f)
    val noPricePreference = mutableStateOf(false)

    val selectedDates = mutableStateListOf(4, 5)
    val timeSlotsByDate = mutableStateMapOf(
        4 to mutableStateListOf(TimeSlot.ANY),
        5 to mutableStateListOf(TimeSlot.MORNING, TimeSlot.AFTERNOON)
    )

    val requestNote = mutableStateOf("4일 18시 이후면 좋겠어요.\n색 변경하고 싶어요.")

    val neighborhood = mutableStateOf("상도동")
    val searchRadius = mutableStateOf(SearchRadius.WIDE)

    val designTags = mutableStateListOf<String>()

    val selectedPhotos get() = selectedPhotoIds.mapNotNull { id -> demoGallery.find { it.id == id } }

    /** 선택된 사진이 바뀔 때마다 호출해서 디자인 요약 태그를 최대 3개까지 다시 계산한다 */
    fun syncDesignTags() {
        designTags.clear()
        designTags.addAll(selectedPhotos.flatMap { it.tags }.take(3))
    }

    fun toggleRemoval(option: RemovalOption) {
        if (option == RemovalOption.NONE) {
            if (removalOptions.contains(RemovalOption.NONE)) {
                removalOptions.remove(RemovalOption.NONE)
            } else {
                removalOptions.clear()
                removalOptions.add(RemovalOption.NONE)
            }
        } else {
            removalOptions.remove(RemovalOption.NONE)
            if (removalOptions.contains(option)) removalOptions.remove(option) else removalOptions.add(option)
        }
    }

    fun timeSlotsFor(day: Int) = timeSlotsByDate.getOrPut(day) { mutableStateListOf() }

    fun toggleDate(day: Int) {
        if (selectedDates.contains(day)) {
            selectedDates.remove(day)
            timeSlotsByDate.remove(day)
        } else {
            selectedDates.add(day)
            timeSlotsByDate[day] = mutableStateListOf(TimeSlot.ANY)
        }
    }

    fun toggleTimeSlot(day: Int, slot: TimeSlot) {
        val slots = timeSlotsFor(day)
        if (slot == TimeSlot.ANY) {
            slots.clear()
            slots.add(TimeSlot.ANY)
            return
        }
        slots.remove(TimeSlot.ANY)
        if (slots.contains(slot)) {
            slots.remove(slot)
            if (slots.isEmpty()) slots.add(TimeSlot.ANY)
        } else {
            slots.add(slot)
        }
    }

    fun scheduleSummary(): String = selectedDates.sorted().joinToString(" | ") { day ->
        val slots = timeSlotsFor(day).joinToString(",") { it.label }
        "${dateLabel(day)} · $slots"
    }

    fun scheduleSummaryMultiline(): String = selectedDates.sorted().joinToString("\n") { day ->
        val slots = timeSlotsFor(day).joinToString(",") { it.label }
        "${dateLabel(day)} · $slots"
    }
}
