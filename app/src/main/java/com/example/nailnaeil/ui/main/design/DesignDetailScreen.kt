package com.example.nailnaeil.ui.main.design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.DesignProposal
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignDetailScreen(
    designId: Long,
    onBackClick: () -> Unit,
    onStartEstimate: () -> Unit,
    onProposalClick: (Long) -> Unit = {},
    viewModel: DesignDetailViewModel = viewModel { DesignDetailViewModel(designId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val design = uiState.design

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("디자인") },
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
                design == null -> Text(
                    text = uiState.errorMessage ?: "디자인 정보를 불러오지 못했어요.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = design.imageUrl,
                        contentDescription = design.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )

                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = design.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Icon(
                                imageVector = if (design.isBookmarked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "찜하기",
                                tint = if (design.isBookmarked) MutedRosePrimary else TextSecondary,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable { viewModel.toggleWish() }
                            )
                        }
                        Text(
                            text = "조회 ${design.viewCount}  ·  찜 ${design.wishCount}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        if (design.tags.isNotEmpty()) {
                            Row(modifier = Modifier.padding(top = 12.dp)) {
                                design.tags.forEach { tag ->
                                    Text(
                                        text = "#$tag",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MutedRosePrimary,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }
                            }
                        }

                        if (!design.description.isNullOrBlank()) {
                            Text(
                                text = design.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        }

                        Button(
                            onClick = onStartEstimate,
                            colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                                .height(52.dp)
                        ) {
                            Text("이 디자인으로 견적 요청하기", color = SurfaceWhite, fontWeight = FontWeight.Bold)
                        }

                        if (design.recentProposals.isNotEmpty()) {
                            Text(
                                text = "최근 견적 사례",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 32.dp, bottom = 12.dp)
                            )
                            design.recentProposals.forEach { proposal ->
                                RecentProposalRow(proposal = proposal, onClick = { onProposalClick(proposal.proposalId) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentProposalRow(proposal: DesignProposal, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = proposal.shop.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(text = proposal.shop.address, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Text(
            text = "${"%,d".format(proposal.price)}원",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MutedRosePrimary
        )
    }
}
