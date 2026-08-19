package com.example.nailnaeil.ui.main.admin

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.data.demo.DemoEngine
import com.example.nailnaeil.di.AppContainer
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSettingsScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val tokenStore = AppContainer.tokenStore
    var syncKey by remember { mutableStateOf(tokenStore.adminSyncKey ?: "") }
    var designKey by remember { mutableStateOf(tokenStore.adminDesignKey ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("관리자 키 설정") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "매장/매거진 관리 API는 로그인 토큰이 아니라 별도의 관리자 키가 필요해요. " +
                    "백엔드 담당자에게 발급받은 키를 입력해주세요.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Text(
                text = "x-admin-sync-key (매장 관리)",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 24.dp)
            )
            OutlinedTextField(
                value = syncKey,
                onValueChange = { syncKey = it },
                singleLine = true,
                placeholder = { Text("샵 관리자 키를 입력해주세요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Text(
                text = "x-admin-design-key (매거진 관리)",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 20.dp)
            )
            OutlinedTextField(
                value = designKey,
                onValueChange = { designKey = it },
                singleLine = true,
                placeholder = { Text("디자인 관리자 키를 입력해주세요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Button(
                onClick = {
                    tokenStore.adminSyncKey = syncKey.ifBlank { null }
                    tokenStore.adminDesignKey = designKey.ifBlank { null }
                    Toast.makeText(context, "저장했어요.", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Text("저장", color = SurfaceWhite, fontWeight = FontWeight.Bold)
            }

            HorizontalDivider(modifier = Modifier.padding(top = 32.dp, bottom = 24.dp))

            Text(
                text = "데모 모드",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "켜면 매장 탐색이 실서버 대신 데모용 10개 매장으로 대체되고, 견적 요청 시 약 10초 후 자동으로 매장 응답이 도착해요. " +
                    "현장에서 문제가 생기면 꺼서 즉시 실서버 흐름으로 되돌릴 수 있어요.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "데모 모드 사용", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Switch(
                    checked = DemoEngine.demoModeEnabled.value,
                    onCheckedChange = { DemoEngine.demoModeEnabled.value = it },
                    colors = SwitchDefaults.colors(checkedTrackColor = MutedRosePrimary)
                )
            }
        }
    }
}
