package com.example.nailnail.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// 와이어프레임에 기반한 기본 테마 정의
private val WireframeColorScheme = lightColorScheme(
    primary = MutedRosePrimary,   // #996677 (핵심 버튼용)
    background = AppBackground,   // #EEEEEE (전체 배경용)
    surface = SurfaceWhite,       // #FFFFFF (카드나 컨테이너용)
    onPrimary = SurfaceWhite,     // 메인 버튼 위의 글자색 (흰색)
    onBackground = TextMain,      // 일반 배경 위의 글자색 (검은색)
    onSurface = TextMain          // 흰색 카드 위의 글자색 (검은색)
)

@Composable
fun NailNailTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WireframeColorScheme,
        content = content
    )
}
