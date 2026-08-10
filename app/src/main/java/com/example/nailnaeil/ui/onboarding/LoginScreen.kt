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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.R
import com.example.nailnaeil.ui.common.BrandLogo
import com.example.nailnaeil.ui.theme.KakaoTextBrown
import com.example.nailnaeil.ui.theme.KakaoYellow
import com.example.nailnaeil.ui.theme.NaverGreen
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun LoginScreen(onKakaoLoginClick: () -> Unit, onNaverLoginClick: () -> Unit) {
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

        Button(
            onClick = onNaverLoginClick,
            colors = ButtonDefaults.buttonColors(containerColor = NaverGreen, contentColor = SurfaceWhite),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(52.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_naver),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Text("네이버로 로그인", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
        }
    }
}
