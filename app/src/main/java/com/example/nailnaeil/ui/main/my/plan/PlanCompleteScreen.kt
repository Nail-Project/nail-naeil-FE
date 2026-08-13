package com.example.nailnaeil.ui.main.my.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.theme.ChipBackground
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun PlanCompleteScreen(
    onBackClick: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().background(SurfaceWhite)) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(20.dp)
                    .clickable(onClick = onBackClick)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MutedRosePrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Celebration,
                    contentDescription = null,
                    tint = MutedRosePrimary,
                    modifier = Modifier.size(36.dp)
                )
            }
            Text(
                text = "네일내일 N 플러스 요금제 가입 완료!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "이제 더 많은 견적을 자유롭게 받아보세요.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ChipBackground)
                    .padding(20.dp)
            ) {
                Text(
                    text = "플러스 요금제 혜택",
                    style = MaterialTheme.typography.labelMedium,
                    color = MutedRosePrimary,
                    fontWeight = FontWeight.Bold
                )
                BenefitRow(
                    icon = Icons.Filled.Image,
                    title = "네일 디자인 이미지 최대 3개 첨부 가능",
                    description = "견적 요청 시 더 다양한 디자인을 한 번에",
                    modifier = Modifier.padding(top = 18.dp)
                )
                BenefitRow(
                    icon = Icons.Filled.AllInclusive,
                    title = "견적 요청 가능 샵 범위 제한 없음",
                    description = "거리에 제한되지 않고 원하는 샵 어디든",
                    modifier = Modifier.padding(top = 18.dp)
                )
                BenefitRow(
                    icon = Icons.Filled.ContentCopy,
                    title = "한 번에 최대 3개의 동시 견적 요청",
                    description = "견적 응답 기다리는 동안 다른 디자인의 견적도 동시에",
                    modifier = Modifier.padding(top = 18.dp)
                )
            }
        }

        PrimaryBottomButton(
            text = "확인",
            onClick = onConfirm,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun BenefitRow(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SurfaceWhite),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MutedRosePrimary, modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextMain)
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
