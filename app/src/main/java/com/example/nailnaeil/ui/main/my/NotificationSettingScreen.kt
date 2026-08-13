package com.example.nailnaeil.ui.main.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nailnaeil.ui.theme.AppBackground
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingScreen(
    onBackClick: () -> Unit,
    viewModel: NotificationSettingViewModel = viewModel { NotificationSettingViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // 서버는 estimate/reservation/marketing 3개 플래그만 지원한다. 채널별(푸시/SNS/이메일) 세부 설정은
    // 대응하는 API가 없어 로컬 상태로만 유지하고, "전체 알림 받기"만 marketingEnabled와 동기화한다.
    var pushNotification by remember { mutableStateOf(false) }
    var snsNotification by remember { mutableStateOf(false) }
    var emailNotification by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("알림 설정") },
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
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "서비스 알림", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                NotificationSwitchRow(
                    title = "예약 알림",
                    description = "예약 확정 및 방문 전 필요한 정보를 안내해 드려요.",
                    checked = uiState.reservationEnabled,
                    onCheckedChange = { viewModel.setReservationEnabled(it) },
                    modifier = Modifier.padding(top = 20.dp)
                )
                NotificationSwitchRow(
                    title = "견적 알림",
                    description = "견적에 대한 응답이 오는 대로 안내해 드려요.",
                    checked = uiState.estimateEnabled,
                    onCheckedChange = { viewModel.setEstimateEnabled(it) },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(AppBackground)
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "광고성 정보 알림", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    TextButton(onClick = { showTermsDialog = true }) {
                        Text("약관 보기", color = MutedRosePrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "전체 알림 받기", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Switch(
                        checked = uiState.marketingEnabled,
                        onCheckedChange = { checked ->
                            viewModel.setMarketingEnabled(checked)
                            pushNotification = checked
                            snsNotification = checked
                            emailNotification = checked
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = MutedRosePrimary, checkedTrackColor = MutedRosePrimary.copy(alpha = 0.3f))
                    )
                }

                AdSubSwitchRow(title = "푸시알림", checked = pushNotification, enabled = uiState.marketingEnabled, onCheckedChange = { pushNotification = it })
                AdSubSwitchRow(title = "SNS", checked = snsNotification, enabled = uiState.marketingEnabled, onCheckedChange = { snsNotification = it })
                AdSubSwitchRow(title = "이메일", checked = emailNotification, enabled = uiState.marketingEnabled, onCheckedChange = { emailNotification = it })
            }
        }
    }

    if (showTermsDialog) {
        Dialog(onDismissRequest = { showTermsDialog = false }) {
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .background(SurfaceWhite)
                    .padding(24.dp)
            ) {
                Text(
                    text = "광고성 정보 알림 약관",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "내용",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp)
                )
            }
        }
    }
}

@Composable
private fun NotificationSwitchRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = MutedRosePrimary, checkedTrackColor = MutedRosePrimary.copy(alpha = 0.3f))
        )
    }
}

@Composable
private fun AdSubSwitchRow(title: String, checked: Boolean, enabled: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.6f)
            .padding(top = 12.dp, start = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(checkedThumbColor = MutedRosePrimary, checkedTrackColor = MutedRosePrimary.copy(alpha = 0.3f))
        )
    }
}
