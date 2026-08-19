package com.example.nailnaeil.ui.quote.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nailnaeil.ui.theme.BorderLight
import com.example.nailnaeil.ui.theme.ButtonDisabled
import com.example.nailnaeil.ui.theme.ChipBackground
import com.example.nailnaeil.ui.theme.ChipSelectedBg
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextDisabled
import com.example.nailnaeil.ui.theme.TextMain
import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

/** X - 타이틀 - > 형태의 1단계(사진 업로드) 헤더 */
@Composable
fun CloseTitleHeader(
    title: String,
    onClose: () -> Unit,
    onNext: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "닫기")
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(onClick = { onNext?.invoke() }, enabled = onNext != null) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "다음",
                tint = if (onNext != null) TextMain else TextDisabled
            )
        }
    }
}

/** < - 타이틀 형태의 헤더 (2~4단계 공통) */
@Composable
fun BackTitleHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Box(Modifier.size(48.dp))
    }
}

/** 상단 4단계 진행 표시 바 */
@Composable
fun QuoteProgressBar(current: Int, total: Int = 4, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(total) { index ->
            Box(
                Modifier
                    .weight(1f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (index < current) MutedRosePrimary else BorderLight)
            )
        }
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain, modifier = modifier)
}

/** 선택 가능한 알약형 칩 (제거/시간대 등에서 재사용) */
@Composable
fun SelectableChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) ChipSelectedBg else SurfaceWhite)
            .border(
                width = 1.dp,
                color = if (selected) MutedRosePrimary else BorderLight,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) MutedRosePrimary else TextMain,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

/** 삭제 불가능한 단순 표시용 태그 */
@Composable
fun DisplayTag(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ChipBackground)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = label, fontSize = 13.sp, color = TextMain)
    }
}

/** X 를 포함한 삭제 가능한 태그 */
@Composable
fun RemovableTag(label: String, onRemove: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ChipBackground)
            .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = label, fontSize = 13.sp, color = TextMain)
        Icon(
            Icons.Filled.Close,
            contentDescription = "삭제",
            modifier = Modifier
                .size(14.dp)
                .clickable(onClick = onRemove),
            tint = TextDisabled
        )
    }
}

@Composable
fun PrimaryBottomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MutedRosePrimary,
            contentColor = SurfaceWhite,
            disabledContainerColor = ButtonDisabled,
            disabledContentColor = SurfaceWhite
        )
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun RowScope.OptionCard(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) ChipSelectedBg else SurfaceWhite)
            .border(
                width = 1.dp,
                color = if (selected) MutedRosePrimary else BorderLight,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            icon?.invoke()
            Text(
                text = label,
                color = if (selected) MutedRosePrimary else TextMain,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun ChangeButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(text = "변경", fontSize = 13.sp, color = TextMain)
    }
}

@Composable
fun ErrorText(text: String, modifier: Modifier = Modifier) {
    Text(text = text, color = Color(0xFFD32F2F), fontSize = 12.sp, modifier = modifier.padding(top = 6.dp))
}

/** 원형 흰색 손잡이 + 연속된 선 형태의 가격 범위 듀얼 슬라이더 (Material3 기본 RangeSlider의 눈금 표시를 대체) */
@Composable
fun PriceRangeSlider(
    lower: Float,
    upper: Float,
    onLowerChange: (Float) -> Unit,
    onUpperChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    step: Float = 1_000f
) {
    val density = LocalDensity.current
    var trackWidthPx by remember { mutableFloatStateOf(0f) }
    val thumbSizeDp = 20.dp
    val thumbRadiusPx = with(density) { (thumbSizeDp / 2).toPx() }
    val usableWidthPx = (trackWidthPx - thumbRadiusPx * 2).coerceAtLeast(1f)
    val rangeSpan = valueRange.endInclusive - valueRange.start

    fun snap(value: Float): Float = (Math.round(value / step) * step).coerceIn(valueRange.start, valueRange.endInclusive)

    val lowerFraction = ((lower - valueRange.start) / rangeSpan).coerceIn(0f, 1f)
    val upperFraction = ((upper - valueRange.start) / rangeSpan).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .onSizeChanged { trackWidthPx = it.width.toFloat() },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            Modifier
                .padding(horizontal = thumbSizeDp / 2)
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(BorderLight)
        )
        if (trackWidthPx > 0f) {
            val activeTrackOffsetPx = thumbRadiusPx + lowerFraction * usableWidthPx
            Box(
                Modifier
                    .offset { IntOffset(activeTrackOffsetPx.roundToInt(), 0) }
                    .width(with(density) { ((upperFraction - lowerFraction) * usableWidthPx).toDp() })
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MutedRosePrimary)
            )
            RangeSliderThumb(
                xOffsetPx = lowerFraction * usableWidthPx,
                size = thumbSizeDp,
                onDragPx = { deltaPx ->
                    val newValue = snap(lower + deltaPx / usableWidthPx * rangeSpan).coerceIn(valueRange.start, upper)
                    onLowerChange(newValue)
                }
            )
            RangeSliderThumb(
                xOffsetPx = upperFraction * usableWidthPx,
                size = thumbSizeDp,
                onDragPx = { deltaPx ->
                    val newValue = snap(upper + deltaPx / usableWidthPx * rangeSpan).coerceIn(lower, valueRange.endInclusive)
                    onUpperChange(newValue)
                }
            )
        }
    }
}

@Composable
private fun RangeSliderThumb(xOffsetPx: Float, size: androidx.compose.ui.unit.Dp, onDragPx: (Float) -> Unit) {
    val draggableState = rememberDraggableState { delta -> onDragPx(delta) }
    Box(
        modifier = Modifier
            .offset { IntOffset(xOffsetPx.roundToInt(), 0) }
            .size(size)
            .shadow(elevation = 2.dp, shape = CircleShape, clip = false)
            .clip(CircleShape)
            .background(SurfaceWhite)
            .border(1.dp, BorderLight, CircleShape)
            .draggable(state = draggableState, orientation = Orientation.Horizontal)
    )
}

/** 탐색 범위 등에서 사용하는 점-선 형태의 단계 게이지 */
@Composable
fun DotRangeGauge(
    level: Int,
    total: Int,
    onLevelChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth().height(28.dp), contentAlignment = Alignment.CenterStart) {
        Canvas(modifier = Modifier.fillMaxWidth().height(2.dp)) {
            drawLine(
                color = BorderLight,
                start = androidx.compose.ui.geometry.Offset(0f, size.height / 2f),
                end = androidx.compose.ui.geometry.Offset(size.width, size.height / 2f),
                strokeWidth = size.height
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            repeat(total) { index ->
                val active = index == level
                Box(
                    modifier = Modifier
                        .size(if (active) 18.dp else 10.dp)
                        .clip(CircleShape)
                        .background(if (active) MutedRosePrimary else SurfaceWhite)
                        .border(1.dp, if (active) MutedRosePrimary else BorderLight, CircleShape)
                        .clickable { onLevelChange(index) }
                )
            }
        }
    }
}
