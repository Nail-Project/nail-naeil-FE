package com.example.nailnaeil.ui.main.my.plan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.R
import com.example.nailnaeil.ui.theme.BorderLight
import com.example.nailnaeil.ui.theme.ButtonDisabled
import com.example.nailnaeil.ui.theme.ChipBackground
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextDisabled
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun PlanSelectionScreen(
    onBackClick: () -> Unit,
    onSelectNPlus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .verticalScroll(rememberScrollState())
    ) {
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

        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.height(40.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.img_profile),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, SurfaceWhite, CircleShape)
                )
                Image(
                    painter = painterResource(id = R.drawable.img_profile),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .offset(x = 22.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, SurfaceWhite, CircleShape)
                )
            }
            Text(
                text = "선택",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 30.dp)
            )
        }

        Text(
            text = "업그레이드 시 더 많은 견적을 한 번에 요청할 수 있어요.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(top = 8.dp, start = 20.dp, end = 20.dp)
        )

        PlanCard(
            badgeText = "추천",
            badgeContainerColor = MutedRosePrimary.copy(alpha = 0.12f),
            badgeTextColor = MutedRosePrimary,
            borderColor = MutedRosePrimary,
            title = "N 플러스",
            titleColor = TextMain,
            subtitle = "더 많은 견적을 한 번에 요청할 수 있어요.",
            bullets = listOf(
                "견적 요청 시 네일 디자인 이미지 최대 3개까지 첨부 가능",
                "견적 요청 가능 범위 제한 없음",
                "한 번에 최대 3개의 견적 요청 가능"
            ),
            priceText = "월 3,200원",
            buttonText = "시작하기",
            buttonEnabled = true,
            onButtonClick = onSelectNPlus,
            modifier = Modifier.padding(top = 24.dp, start = 20.dp, end = 20.dp)
        )

        PlanCard(
            badgeText = "적용됨",
            badgeContainerColor = ChipBackground,
            badgeTextColor = TextSecondary,
            borderColor = BorderLight,
            title = "무료",
            titleColor = TextMain,
            subtitle = "네일내일 모두가 누릴 수 있는 기본 플랜이에요.",
            bullets = listOf(
                "견적 요청 시 네일 디자인 이미지 최대 1개 첨부 가능",
                "견적 요청 가능 범위 5km, 10km로 제한",
                "한 번에 최대 1개의 견적 요청 가능"
            ),
            priceText = null,
            buttonText = "현재 플랜입니다.",
            buttonEnabled = false,
            onButtonClick = {},
            modifier = Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
        )
    }
}

@Composable
private fun PlanCard(
    badgeText: String,
    badgeContainerColor: androidx.compose.ui.graphics.Color,
    badgeTextColor: androidx.compose.ui.graphics.Color,
    borderColor: androidx.compose.ui.graphics.Color,
    title: String,
    titleColor: androidx.compose.ui.graphics.Color,
    subtitle: String,
    bullets: List<String>,
    priceText: String?,
    buttonText: String,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Text(
            text = badgeText,
            style = MaterialTheme.typography.labelSmall,
            color = badgeTextColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(badgeContainerColor)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = titleColor,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )
        Column(modifier = Modifier.padding(top = 16.dp)) {
            bullets.forEach { bullet ->
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = if (buttonEnabled) MutedRosePrimary else TextDisabled,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = bullet,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMain,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        if (priceText != null) {
            Text(
                text = priceText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MutedRosePrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        Button(
            onClick = onButtonClick,
            enabled = buttonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MutedRosePrimary,
                contentColor = SurfaceWhite,
                disabledContainerColor = ButtonDisabled,
                disabledContentColor = SurfaceWhite
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (priceText != null) 16.dp else 20.dp)
                .height(52.dp)
        ) {
            Text(text = buttonText, fontWeight = FontWeight.Bold)
        }
    }
}
