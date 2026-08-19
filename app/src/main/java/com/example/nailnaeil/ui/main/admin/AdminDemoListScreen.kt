package com.example.nailnaeil.ui.main.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nailnaeil.data.demo.DemoAdminRequestSummary
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDemoListScreen(
    onBackClick: () -> Unit,
    viewModel: AdminDemoListViewModel = viewModel { AdminDemoListViewModel() }
) {
    val requests by viewModel.activeRequests.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("데모 관리") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        if (requests.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    text = "아직 발생한 데모 견적 요청이 없어요.\n앱에서 데모 견적을 요청하면 여기에 표시돼요.",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
            ) {
                items(requests, key = { it.requestId }) { request ->
                    DemoRequestRow(
                        request = request,
                        onForceRespond = { viewModel.forceRespondNow(request.requestId) },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DemoRequestRow(
    request: DemoAdminRequestSummary,
    onForceRespond: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceWhite)
            .border(1.dp, MutedRoseBgLight, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(text = request.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Text(
            text = "요청 시각: ${request.createdAt}",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "응답 완료 ${request.respondedCount} / ${request.totalShops}곳" +
                    if (request.waitingCount > 0) " · 대기 중 ${request.waitingCount}곳" else " · 전부 응답 완료",
                style = MaterialTheme.typography.bodyMedium
            )
            if (request.waitingCount > 0) {
                Button(
                    onClick = onForceRespond,
                    colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary)
                ) {
                    Text("지금 모두 응답 도착시키기", color = SurfaceWhite)
                }
            }
        }
    }
}
