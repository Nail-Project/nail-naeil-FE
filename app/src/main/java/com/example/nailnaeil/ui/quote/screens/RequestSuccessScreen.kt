package com.example.nailnaeil.ui.quote.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SuccessGreenBg
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun RequestSuccessScreen(
    neighborhood: String,
    shopCount: Int,
    onGoToQuoteBox: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().background(SurfaceWhite)) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(SuccessGreenBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = MutedRosePrimary, modifier = Modifier.size(32.dp))
            }
            Text(
                text = "견적을 요청했어요",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "$neighborhood 주변 ${shopCount}개 샵이 가격과 가능 시간을 확인 중이예요\n답변이 오는대로 알려드릴게요",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp, start = 32.dp, end = 32.dp)
            )
            Box(modifier = Modifier.weight(1f))
        }
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = "샵 상황에 따라 최대 72시간 소요됩니다",
                fontSize = 11.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )
            PrimaryBottomButton(text = "견적함 바로가기", onClick = onGoToQuoteBox)
        }
    }
}
