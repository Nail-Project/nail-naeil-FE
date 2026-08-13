package com.example.nailnaeil.ui.quote.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.NailType
import com.example.nailnaeil.data.remote.dto.RemovalType
import com.example.nailnaeil.ui.main.estimate.label
import com.example.nailnaeil.ui.quote.QuoteUiState
import com.example.nailnaeil.ui.quote.components.BackTitleHeader
import com.example.nailnaeil.ui.quote.components.ErrorText
import com.example.nailnaeil.ui.quote.components.OptionCard
import com.example.nailnaeil.ui.quote.components.PriceRangeSlider
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.quote.components.QuoteProgressBar
import com.example.nailnaeil.ui.quote.components.SectionTitle
import com.example.nailnaeil.ui.theme.BorderLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TreatmentInfoScreen(
    state: QuoteUiState,
    onBack: () -> Unit,
    onAddMorePhotos: () -> Unit,
    onNext: () -> Unit,
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
            Text(text = "시술 정보를 알려주세요", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextMain)
            Text(
                text = "샵이 정확한 견적을 보낼 수 있도록 필요한 정보만 확인할게요.",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
            )

            Text(text = "선택한 디자인 (${state.selectedPhotoUris.size}/3)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.selectedPhotoUris.forEach { uri ->
                    Box(modifier = Modifier.size(80.dp)) {
                        AsyncImage(
                            model = uri,
                            contentDescription = "선택한 사진",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(SurfaceWhite)
                                .clickable { state.selectedPhotoUris.remove(uri) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = "제거", modifier = Modifier.size(12.dp), tint = TextMain)
                        }
                    }
                }
                if (state.selectedPhotoUris.size < 3) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, BorderLight, RoundedCornerShape(10.dp))
                            .clickable(onClick = onAddMorePhotos),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Add, contentDescription = "추가", tint = TextSecondary)
                            Text(text = "추가하기", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }
            }

            SectionTitle(text = "시술 부위", modifier = Modifier.padding(top = 28.dp))
            Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                NailType.entries.forEach { type ->
                    OptionCard(
                        label = type.label(),
                        selected = state.nailType.value == type,
                        onClick = { state.nailType.value = type }
                    )
                }
            }

            Row(modifier = Modifier.padding(top = 28.dp)) {
                Text(text = "제거 필요", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = " (중복 선택 가능)", fontSize = 12.sp, color = TextSecondary)
            }
            Column(modifier = Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                RemovalType.entries.chunked(2).forEach { rowOptions ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        rowOptions.forEach { option ->
                            OptionCard(
                                label = option.label(),
                                selected = state.removalTypes.contains(option),
                                onClick = { state.toggleRemoval(option) }
                            )
                        }
                    }
                }
            }
            if (state.removalTypes.isEmpty()) {
                ErrorText(text = "제거 여부를 하나 이상 선택해주세요")
            }

            SectionTitle(text = "예상 가격 범위 설정", modifier = Modifier.padding(top = 28.dp))
            Text(
                text = "예산안을 생각하며 예상 가격을 설정하면, 샵에서 참고할게요",
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(com.example.nailnaeil.ui.theme.AppBackground)
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "가격 범위",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "${formatWon(state.priceLower.floatValue)} ~ ${formatWon(state.priceUpper.floatValue)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp, bottom = 8.dp)
                    )
                    PriceRangeSlider(
                        lower = state.priceLower.floatValue,
                        upper = state.priceUpper.floatValue,
                        onLowerChange = {
                            state.priceLower.floatValue = it
                            state.noPricePreference.value = false
                        },
                        onUpperChange = {
                            state.priceUpper.floatValue = it
                            state.noPricePreference.value = false
                        },
                        valueRange = 50_000f..150_000f
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "50,000원", fontSize = 11.sp, color = TextSecondary)
                        Text(text = "100,000원", fontSize = 11.sp, color = TextSecondary)
                        Text(text = "150,000원 ~", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
            Text(
                text = if (state.noPricePreference.value) "상관없음 (선택됨)" else "상관없음",
                fontSize = 13.sp,
                color = if (state.noPricePreference.value) MutedRosePrimary else TextMain,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 10.dp)
                    .clickable {
                        state.noPricePreference.value = true
                        state.priceLower.floatValue = 50_000f
                        state.priceUpper.floatValue = 150_000f
                    }
            )
            Box(modifier = Modifier.size(1.dp, 12.dp))
        }

        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            PrimaryBottomButton(text = "다음", onClick = onNext, enabled = state.removalTypes.isNotEmpty())
        }
    }
}

private fun formatWon(value: Float): String {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
    return "${formatter.format(value.toInt())}원"
}
