package com.example.nailnail.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.SurfaceWhite

/**
 * 실제 로고 이미지 자산이 없어 임시로 사용하는 브랜드 로고.
 * 자산이 준비되면 이 컴포저블만 이미지로 교체하면 된다.
 */
@Composable
fun BrandLogo(modifier: Modifier = Modifier, size: Dp = 96.dp) {
    Box(
        modifier = modifier
            .size(size)
            .background(MutedRosePrimary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "N",
            color = SurfaceWhite,
            fontSize = (size.value / 2).sp,
            fontWeight = FontWeight.Bold
        )
    }
}
