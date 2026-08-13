package com.example.nailnaeil.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.di.AppContainer
import com.example.nailnaeil.ui.common.BrandLogo
import com.example.nailnaeil.ui.theme.KakaoTextBrown
import com.example.nailnaeil.ui.theme.KakaoYellow
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onKakaoLoginClick: () -> Unit,
    onTestLoginSuccess: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var isTestLoggingIn by remember { mutableStateOf(false) }
    var testLoginError by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BrandLogo()
        Text(
            text = "네일내일",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Your nail care companion",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(top = 8.dp, bottom = 56.dp)
        )

        Button(
            onClick = onKakaoLoginClick,
            colors = ButtonDefaults.buttonColors(containerColor = KakaoYellow, contentColor = KakaoTextBrown),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("카카오로 시작하기", fontWeight = FontWeight.Bold)
        }

        // TODO: 백엔드 연동 테스트용 임시 버튼. 확인 끝나면 제거할 것.
        OutlinedButton(
            onClick = {
                if (isTestLoggingIn) return@OutlinedButton
                isTestLoggingIn = true
                testLoginError = null
                scope.launch {
                    AppContainer.authRepository.login(identifier = "demo_master", password = "Demo1234!")
                        .onSuccess { onTestLoginSuccess() }
                        .onFailure { e -> testLoginError = e.message ?: "로그인에 실패했어요." }
                    isTestLoggingIn = false
                }
            },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MutedRosePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(52.dp)
        ) {
            if (isTestLoggingIn) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MutedRosePrimary)
            } else {
                Text("테스트 로그인 (demo_master)", fontWeight = FontWeight.Bold)
            }
        }
        if (testLoginError != null) {
            Text(
                text = testLoginError.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
