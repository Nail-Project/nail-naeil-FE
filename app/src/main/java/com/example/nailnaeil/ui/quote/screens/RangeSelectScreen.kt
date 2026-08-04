package com.example.nailnaeil.ui.quote.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nailnaeil.ui.quote.QuoteUiState
import com.example.nailnaeil.ui.quote.SearchRadius
import com.example.nailnaeil.ui.quote.components.BackTitleHeader
import com.example.nailnaeil.ui.quote.components.DotRangeGauge
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.quote.components.QuoteProgressBar
import com.example.nailnaeil.ui.theme.BorderLight
import com.example.nailnaeil.ui.theme.MapBackground
import com.example.nailnaeil.ui.theme.MapRoad
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun RangeSelectScreen(
    state: QuoteUiState,
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().background(SurfaceWhite)) {
        BackTitleHeader(title = "탐색 범위 지정", onBack = onBack)
        QuoteProgressBar(current = 3, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(text = "견적 받을 네일샵 범위", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain)
            OutlinedTextField(
                value = state.neighborhood.value,
                onValueChange = { state.neighborhood.value = it },
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                singleLine = true,
                leadingIcon = null,
                trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "동네 검색") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MutedRosePrimary,
                    unfocusedBorderColor = BorderLight
                )
            )
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(top = 16.dp)) {
            FakeMap(radiusLevel = SearchRadius.entries.indexOf(state.searchRadius.value))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(SurfaceWhite)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = state.searchRadius.value.headline,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = state.searchRadius.value.subtitle,
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp, bottom = 16.dp)
            )
            DotRangeGauge(
                level = SearchRadius.entries.indexOf(state.searchRadius.value),
                total = SearchRadius.entries.size,
                onLevelChange = { level -> state.searchRadius.value = SearchRadius.entries[level] }
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "가까운 네일샵", fontSize = 11.sp, color = TextSecondary)
                Text(text = "먼 네일샵", fontSize = 11.sp, color = TextSecondary)
            }
            PrimaryBottomButton(
                text = state.searchRadius.value.buttonLabel,
                onClick = onSubmit,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
private fun FakeMap(radiusLevel: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().clipToBounds().background(MapBackground)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width * 0.45f, size.height * 0.4f)
            drawLine(MapRoad, Offset(0f, size.height * 0.55f), Offset(size.width, size.height * 0.3f), strokeWidth = 10f)
            drawLine(Color(0xFFCFCAC4), Offset(size.width * 0.2f, 0f), Offset(size.width * 0.75f, size.height), strokeWidth = 6f)

            val fraction = (radiusLevel + 1f) / 4f
            val maxRadius = size.minDimension * 0.55f
            val radius = maxRadius * fraction
            drawCircle(color = MutedRosePrimary.copy(alpha = 0.15f), radius = radius, center = center)
            drawCircle(color = MutedRosePrimary.copy(alpha = 0.5f), radius = radius, center = center, style = Stroke(width = 3f))
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-6).dp, y = (-24).dp)
                .size(28.dp)
                .clip(CircleShape)
                .background(MutedRosePrimary)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(SurfaceWhite),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.MyLocation, contentDescription = "내 위치", tint = MutedRosePrimary)
        }
    }
}
