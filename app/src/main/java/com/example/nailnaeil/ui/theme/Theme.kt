package com.example.nailnaeil.ui.theme

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
    onSurface = TextMain,         // 흰색 카드 위의 글자색 (검은색)
    // Material3 기본값(보라 계열)이 하단 탭바 선택 표시 등에 그대로 노출되지 않도록 명시적으로 지정
    secondary = MutedRosePrimary,
    secondaryContainer = MutedRoseBgLight,
    onSecondaryContainer = MutedRosePrimary,
    surfaceVariant = AppBackground,
    onSurfaceVariant = TextSecondary,
    outline = DividerGray
)

@Composable
fun NailNailTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WireframeColorScheme,
        typography = NailNailTypography,
        content = content
    )
}
