package com.example.nailnaeil.ui.quote.screens

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.EstimateRequest
import com.example.nailnaeil.data.remote.dto.EstimateSchedule
import com.example.nailnaeil.di.AppContainer
import com.example.nailnaeil.ui.main.estimate.label
import com.example.nailnaeil.ui.quote.QuoteUiState
import com.example.nailnaeil.ui.quote.components.BackTitleHeader
import com.example.nailnaeil.ui.quote.components.ChangeButton
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.quote.components.QuoteProgressBar
import com.example.nailnaeil.ui.theme.BorderLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun ConfirmScreen(
    state: QuoteUiState,
    onBack: () -> Unit,
    onShowMoreRequest: () -> Unit,
    onChangeSchedule: () -> Unit,
    onChangeRemoval: () -> Unit,
    onChangePart: () -> Unit,
    onChangeRange: () -> Unit,
    onChangeShops: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.submitError.value) {
        state.submitError.value?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

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
                        val thumbnail = state.selectedPhotoUris.firstOrNull()
                        if (thumbnail != null) {
                            AsyncImage(
                                model = thumbnail,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BorderLight)
                            )
                        }
                        Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                            Text(
                                text = "${state.nailType.value.label()} · ${state.removalTypes.joinToString("/") { it.label() }}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextMain
                            )
                            Text(
                                text = state.requestNote.value.ifBlank { "요청사항 없음" },
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
                        value = state.removalTypes.joinToString("\n") { it.label() }.ifEmpty { "제거 없음" },
                        onChange = onChangeRemoval
                    )
                    ConfirmRow(
                        title = "시술 부위",
                        value = state.nailType.value.label(),
                        onChange = onChangePart
                    )
                    ConfirmRow(
                        title = "샵 범위",
                        value = "${state.nearbyShops.size}개 매장 · ${state.searchRadius.value.rangeLabel}",
                        onChange = onChangeRange
                    )
                    ConfirmRow(
                        title = "매장 선택",
                        value = "${state.selectedShopIds.size}개 매장에 견적 요청",
                        onChange = onChangeShops
                    )
                }
            }
            Box(modifier = Modifier.size(1.dp, 12.dp))
        }

        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            PrimaryBottomButton(
                text = if (state.isSubmitting.value) "요청 중..." else "견적 요청하기",
                enabled = state.removalTypes.isNotEmpty() && state.selectedDates.isNotEmpty() &&
                    state.selectedShopIds.isNotEmpty() && !state.isSubmitting.value,
                onClick = {
                    scope.launch {
                        state.isSubmitting.value = true
                        state.submitError.value = null
                        val request = EstimateRequest(
                            nailType = state.nailType.value,
                            removalTypes = state.removalTypes.toList(),
                            schedules = state.selectedDates.sorted().map { date ->
                                EstimateSchedule(date = date, times = state.timeSlotsFor(date).toList())
                            },
                            recommendType = state.searchRadius.value.recommendType,
                            description = state.requestNote.value.ifBlank { null },
                            radiusMeters = state.searchRadius.value.radiusMeters,
                            priceMin = if (state.noPricePreference.value) null else state.priceLower.floatValue.toInt(),
                            priceMax = if (state.noPricePreference.value) null else state.priceUpper.floatValue.toInt(),
                            images = state.uploadedImageUrls.toList().ifEmpty { null },
                            shopIds = state.selectedShopIds.toList().ifEmpty { null }
                        )
                        AppContainer.estimateRepository.createEstimate(request)
                            .onSuccess { response ->
                                state.isSubmitting.value = false
                                state.submittedEstimate.value = response
                                onSubmit()
                            }
                            .onFailure { e ->
                                state.isSubmitting.value = false
                                state.submitError.value = e.message ?: "견적 요청에 실패했어요."
                            }
                    }
                }
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
