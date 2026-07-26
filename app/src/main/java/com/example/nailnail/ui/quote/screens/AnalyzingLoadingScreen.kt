package com.example.nailnail.ui.quote.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nailnail.ui.quote.QuoteUiState
import com.example.nailnail.ui.quote.components.DisplayTag
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.SurfaceWhite
import com.example.nailnail.ui.theme.TextMain
import kotlinx.coroutines.delay

/**
 * 선택한 사진을 분석하는 로딩 화면. 태그가 하나씩 나타나는 연출 후 다음 단계로 자동 이동한다.
 */
@Composable
fun AnalyzingLoadingScreen(
    state: QuoteUiState,
    onAnalyzed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allTags = state.designTags
    var revealedCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(state.selectedPhotoIds.toList()) {
        revealedCount = 0
        delay(500)
        while (revealedCount < allTags.size) {
            revealedCount++
            delay(500)
        }
        delay(600)
        onAnalyzed()
    }

    Column(
        modifier = modifier.fillMaxSize().background(SurfaceWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 220.dp)) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val photos = state.selectedPhotos
                if (photos.isEmpty()) {
                    CircularProgressIndicator(color = MutedRosePrimary, strokeWidth = 3.dp, modifier = Modifier.size(40.dp))
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy((-14).dp)) {
                        photos.forEach { photo ->
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceWhite)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(photo.color)
                            )
                        }
                    }
                }
                Text(
                    text = "디자인을 분석하고 있어요",
                    modifier = Modifier.padding(top = 20.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allTags.take(revealedCount).forEach { tag ->
                        DisplayTag(label = tag)
                    }
                }
            }
        }
    }
}
