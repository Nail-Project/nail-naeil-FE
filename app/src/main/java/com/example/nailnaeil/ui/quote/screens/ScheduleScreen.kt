package com.example.nailnaeil.ui.quote.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nailnaeil.data.remote.dto.VisitTimeSlot
import com.example.nailnaeil.ui.quote.CalendarDay
import com.example.nailnaeil.ui.quote.QuoteUiState
import com.example.nailnaeil.ui.quote.components.BackTitleHeader
import com.example.nailnaeil.ui.quote.components.ErrorText
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.quote.components.QuoteProgressBar
import com.example.nailnaeil.ui.quote.components.SelectableChip
import com.example.nailnaeil.ui.quote.label
import com.example.nailnaeil.ui.quote.quoteCalendarMonths
import com.example.nailnaeil.ui.theme.ChipBackground
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextDisabled
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun ScheduleScreen(
    state: QuoteUiState,
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().background(SurfaceWhite)) {
        BackTitleHeader(title = "견적 정보", onBack = onBack)
        QuoteProgressBar(current = 1, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "방문 가능한 일정을 선택해주세요", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextMain)
            Text(
                text = "원하는 날짜를 모두 선택해주세요.\n오늘 날짜 기준 최대 7일까지 예약 가능합니다",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
            )

            MonthCalendar(state = state)

            Text(
                text = "오전 : 8-12시   오후 : 12시 - 18시   저녁 : 18시 - 22시",
                fontSize = 11.sp,
                color = TextSecondary,
                modifier = Modifier.padding(vertical = 14.dp)
            )

            state.selectedDates.sorted().forEach { date ->
                Text(text = state.dateLabel(date), fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(top = 6.dp))
                Row(modifier = Modifier.padding(top = 8.dp, bottom = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    VisitTimeSlot.entries.forEach { slot ->
                        SelectableChip(
                            label = slot.label(),
                            selected = state.timeSlotsFor(date).contains(slot),
                            onClick = { state.toggleTimeSlot(date, slot) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (state.selectedDates.isNotEmpty()) {
                Text(
                    text = state.scheduleSummary(),
                    fontSize = 12.sp,
                    color = MutedRosePrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 12.dp)
                )
            } else {
                ErrorText(text = "방문 가능한 날짜를 하나 이상 선택해주세요")
            }

            Row(modifier = Modifier.padding(top = 24.dp)) {
                Text(text = "요청사항 ", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "(선택)", fontSize = 12.sp, color = TextSecondary)
            }
            OutlinedTextField(
                value = state.requestNote.value,
                onValueChange = { state.requestNote.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 20.dp)
                    .height(100.dp),
                placeholder = { Text("컬러를 변경하고 싶어요.\n18시 이후면 좋겠어요.", color = TextSecondary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ChipBackground,
                    unfocusedContainerColor = ChipBackground,
                    focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        }

        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            PrimaryBottomButton(
                text = "샵 설정하기",
                onClick = onSubmit,
                enabled = state.selectedDates.isNotEmpty()
            )
        }
    }
}

@Composable
fun MonthCalendar(state: QuoteUiState, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        quoteCalendarMonths().forEachIndexed { index, month ->
            if (index > 0) Box(modifier = Modifier.height(20.dp))

            Text(
                text = month.label,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 12.dp)
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("일", "월", "화", "수", "목", "금", "토").forEach { weekday ->
                    Text(
                        text = weekday,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            val cells: List<CalendarDay?> = List(month.leadingBlanks) { null } + month.days
            cells.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { day ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .let {
                                    if (day != null && state.selectedDates.contains(day.date)) it.background(MutedRosePrimary) else it
                                }
                                .then(
                                    if (day != null && day.selectable) Modifier.clickable { state.toggleDate(day.date) } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (day != null) {
                                val selected = state.selectedDates.contains(day.date)
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${day.dayOfMonth}",
                                        fontSize = 13.sp,
                                        color = when {
                                            selected -> SurfaceWhite
                                            day.isToday -> MutedRosePrimary
                                            !day.selectable -> TextDisabled
                                            else -> TextMain
                                        },
                                        fontWeight = if (day.isToday || selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (day.isToday) {
                                        Text(text = "오늘", fontSize = 8.sp, color = if (selected) SurfaceWhite else MutedRosePrimary)
                                    }
                                }
                            }
                        }
                    }
                    repeat(7 - week.size) {
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
    }
}
