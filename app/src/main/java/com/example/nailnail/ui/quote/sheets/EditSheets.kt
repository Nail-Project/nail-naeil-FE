package com.example.nailnail.ui.quote.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nailnail.ui.quote.QuoteUiState
import com.example.nailnail.ui.quote.RemovalOption
import com.example.nailnail.ui.quote.TimeSlot
import com.example.nailnail.ui.quote.TreatmentPart
import com.example.nailnail.ui.quote.components.ErrorText
import com.example.nailnail.ui.quote.components.OptionCard
import com.example.nailnail.ui.quote.components.PrimaryBottomButton
import com.example.nailnail.ui.quote.components.SelectableChip
import com.example.nailnail.ui.quote.dateLabel
import com.example.nailnail.ui.quote.screens.MonthCalendar
import com.example.nailnail.ui.theme.BorderLight
import com.example.nailnail.ui.theme.MutedRoseBgLight
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.SurfaceWhite
import com.example.nailnail.ui.theme.TextMain
import com.example.nailnail.ui.theme.TextSecondary

@Composable
private fun SheetTitle(text: String) {
    Text(
        text = text,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = TextMain,
        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

/** 스크롤 가능한 내용 + 화면 하단에 고정된 확인 버튼으로 구성된 바텀시트 뼈대 */
@Composable
private fun EditSheetScaffold(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmLabel: String = "확인",
    content: @Composable () -> Unit
) {
    val maxHeight = (LocalConfiguration.current.screenHeightDp * 0.85f).dp
    Column(modifier = modifier.fillMaxWidth().heightIn(max = maxHeight)) {
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            content()
        }
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
            PrimaryBottomButton(text = confirmLabel, onClick = onConfirm)
        }
    }
}

@Composable
fun ScheduleEditSheet(state: QuoteUiState, onConfirm: () -> Unit, modifier: Modifier = Modifier) {
    EditSheetScaffold(onConfirm = onConfirm, modifier = modifier) {
        SheetTitle("일정 변경")
        HorizontalDivider(color = BorderLight)
        Box(Modifier.height(14.dp))
        MonthCalendar(state = state)
        Text(
            text = "오전 : 8-12시   오후 : 12시 - 18시   저녁 : 18시 - 22시",
            fontSize = 11.sp,
            color = TextSecondary,
            modifier = Modifier.padding(vertical = 14.dp)
        )
        state.selectedDates.sorted().forEach { day ->
            Text(text = dateLabel(day), fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            Row(modifier = Modifier.padding(top = 8.dp, bottom = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TimeSlot.entries.forEach { slot ->
                    SelectableChip(
                        label = slot.label,
                        selected = state.timeSlotsFor(day).contains(slot),
                        onClick = { state.toggleTimeSlot(day, slot) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        if (state.selectedDates.isEmpty()) {
            ErrorText(text = "방문 가능한 날짜를 하나 이상 선택해주세요")
        }
    }
}

@Composable
fun RemovalEditSheet(state: QuoteUiState, onConfirm: () -> Unit, modifier: Modifier = Modifier) {
    EditSheetScaffold(onConfirm = onConfirm, modifier = modifier) {
        SheetTitle("제거 여부 변경")
        HorizontalDivider(color = BorderLight)
        Box(Modifier.height(16.dp))
        RemovalOption.entries.chunked(2).forEach { rowOptions ->
            Row(modifier = Modifier.padding(bottom = 10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowOptions.forEach { option ->
                    OptionCard(
                        label = option.label,
                        selected = state.removalOptions.contains(option),
                        onClick = { state.toggleRemoval(option) }
                    )
                }
            }
        }
        if (state.removalOptions.isEmpty()) {
            ErrorText(text = "제거 여부를 하나 이상 선택해주세요")
        }
    }
}

@Composable
fun TreatmentPartEditSheet(state: QuoteUiState, onConfirm: () -> Unit, modifier: Modifier = Modifier) {
    EditSheetScaffold(onConfirm = onConfirm, modifier = modifier) {
        SheetTitle("시술 부위 변경")
        HorizontalDivider(color = BorderLight)
        Box(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TreatmentPart.entries.forEach { part ->
                OptionCard(
                    label = part.label,
                    selected = state.treatmentPart.value == part,
                    onClick = { state.treatmentPart.value = part }
                )
            }
        }
    }
}

@Composable
fun RequestNoteDialogContent(
    state: QuoteUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editing by remember { mutableStateOf(false) }
    var draft by remember(state.requestNote.value) { mutableStateOf(state.requestNote.value) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite)
            .padding(24.dp)
    ) {
        SheetTitle("요청사항")
        if (editing) {
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                modifier = Modifier.fillMaxWidth().height(110.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MutedRosePrimary,
                    unfocusedBorderColor = BorderLight
                )
            )
            PrimaryBottomButton(
                text = "완료",
                onClick = {
                    state.requestNote.value = draft
                    editing = false
                    onDismiss()
                },
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MutedRoseBgLight)
                    .padding(16.dp)
            ) {
                Text(text = state.requestNote.value, fontSize = 14.sp, color = TextMain, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = { editing = true },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("수정")
                }
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary)
                ) {
                    Text("확인")
                }
            }
        }
    }
}

@Composable
fun ScheduleConfirmDialogContent(
    state: QuoteUiState,
    onBackToEdit: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite)
            .padding(24.dp)
    ) {
        SheetTitle("아래 일정이 맞나요?")
        Text(
            text = "요청 후에는 일정 변경이 어려워요",
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MutedRoseBgLight)
                .padding(16.dp)
        ) {
            state.selectedDates.sorted().forEach { day ->
                Text(
                    text = "${dateLabel(day)} · ${state.timeSlotsFor(day).joinToString(",") { it.label }}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMain,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = onBackToEdit,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("이전으로")
            }
            Button(
                onClick = onConfirm,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary)
            ) {
                Text("확인", color = SurfaceWhite)
            }
        }
    }
}
