package com.example.nailnail.ui.quote.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nailnail.ui.quote.QuoteUiState
import com.example.nailnail.ui.quote.components.BackTitleHeader
import com.example.nailnail.ui.quote.components.ChangeButton
import com.example.nailnail.ui.quote.components.PrimaryBottomButton
import com.example.nailnail.ui.quote.components.QuoteProgressBar
import com.example.nailnail.ui.theme.BorderLight
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.SurfaceWhite
import com.example.nailnail.ui.theme.TextMain
import com.example.nailnail.ui.theme.TextSecondary

@Composable
fun ConfirmScreen(
    state: QuoteUiState,
    onBack: () -> Unit,
    onShowMoreRequest: () -> Unit,
    onChangeSchedule: () -> Unit,
    onChangeRemoval: () -> Unit,
    onChangePart: () -> Unit,
    onChangeRange: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().background(SurfaceWhite)) {
        BackTitleHeader(title = "견적 확인", onBack = onBack)
        QuoteProgressBar(current = 4, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "이 내용으로 문자를 보낼게요", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextMain)
            Text(
                text = "전달될 요청서를 마지막으로 확인해주세요.",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row {
                        val thumbColor = state.selectedPhotos.firstOrNull()?.color ?: BorderLight
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(thumbColor)
                        )
                        Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                            Text(
                                text = state.designTags.ifEmpty { listOf("디자인 미선택") }.joinToString(" | "),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextMain
                            )
                            Text(
                                text = state.requestNote.value,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                maxLines = 2,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Text(
                                text = "더보기",
                                fontSize = 12.sp,
                                color = MutedRosePrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .clickable(onClick = onShowMoreRequest)
                            )
                        }
                    }

                    ConfirmRow(
                        title = "희망 일정",
                        value = state.scheduleSummaryMultiline(),
                        onChange = onChangeSchedule
                    )
                    ConfirmRow(
                        title = "제거",
                        value = state.removalOptions.joinToString("\n") { it.label }.ifEmpty { "제거 없음" },
                        onChange = onChangeRemoval
                    )
                    ConfirmRow(
                        title = "시술 부위",
                        value = state.treatmentPart.value.label,
                        onChange = onChangePart
                    )
                    ConfirmRow(
                        title = "샵 범위",
                        value = "${state.neighborhood.value} 근처 ${state.searchRadius.value.rangeLabel}",
                        onChange = onChangeRange
                    )
                }
            }
            Box(modifier = Modifier.size(1.dp, 12.dp))
        }

        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            PrimaryBottomButton(
                text = "견적 요청하기",
                onClick = onSubmit,
                enabled = state.removalOptions.isNotEmpty() && state.selectedDates.isNotEmpty()
            )
        }
    }
}

@Composable
private fun ConfirmRow(title: String, value: String, onChange: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 22.dp)) {
        HorizontalDivider(color = BorderLight)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextMain)
            ChangeButton(onClick = onChange)
        }
        Text(text = value, fontSize = 13.sp, color = TextSecondary, modifier = Modifier.padding(top = 8.dp))
    }
}
