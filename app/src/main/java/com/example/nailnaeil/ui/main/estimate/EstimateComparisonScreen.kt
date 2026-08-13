package com.example.nailnaeil.ui.main.estimate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.nailnaeil.ui.common.PhotoPlaceholder
import com.example.nailnaeil.ui.main.estimate.components.ShopEstimateCard
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimateComparisonScreen(
    estimateId: String,
    onBackClick: () -> Unit,
    onShopDetailClick: (Long) -> Unit,
    viewModel: EstimateComparisonViewModel = viewModel {
        EstimateComparisonViewModel(estimateId.toLongOrNull() ?: 0L)
    }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val result = uiState.result

    Scaffold(
        containerColor = SurfaceWhite,
        topBar = {
            TopAppBar(
                title = { Text("견적 비교 결과") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                result == null -> Text(
                    text = uiState.errorMessage ?: "견적 정보를 찾을 수 없어요",
                    color = TextSecondary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(SurfaceWhite)
                                .border(1.dp, DividerGray, RoundedCornerShape(20.dp))
                                .padding(20.dp)
                        ) {
                            PhotoPlaceholder(modifier = Modifier.size(64.dp))
                            Column(modifier = Modifier.padding(start = 14.dp)) {
                                Text(
                                    text = result.title ?: "견적 요청",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "제안 ${result.responses.size}건 도착 · 대기 중인 매장 ${result.waitingShops.size}곳",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                        }
                    }

                    if (result.responses.isNotEmpty()) {
                        item {
                            Text(
                                text = "도착한 견적",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(result.responses, key = { it.id }) { response ->
                            ShopEstimateCard(
                                response = response,
                                onDetailClick = { onShopDetailClick(response.id) }
                            )
                        }
                    }

                    if (result.waitingShops.isNotEmpty()) {
                        item {
                            Text(
                                text = "응답 대기 중",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(result.waitingShops, key = { it.shopId }) { waiting ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, DividerGray, RoundedCornerShape(12.dp))
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = waiting.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "평균 응답 ${waiting.averageResponseMinutes}분",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedRosePrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
