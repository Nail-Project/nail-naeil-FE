package com.example.nailnaeil.ui.main.design

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.DesignProposal
import com.example.nailnaeil.ui.theme.BorderLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun RecentProposalsScreen(
    designId: Long,
    onBackClick: () -> Unit,
    onRequestEstimate: (Long) -> Unit,
    viewModel: RecentProposalsViewModel = viewModel { RecentProposalsViewModel(designId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceWhite)) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(20.dp)
                    .clickable(onClick = onBackClick)
            )
            Text(
                text = "최근 견적 사례",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        when {
            uiState.isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            uiState.proposals.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.errorMessage ?: "아직 견적 사례가 없어요.", color = TextSecondary)
            }
            else -> {
                val lowestPrice = uiState.proposals.minOf { it.price }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "총 ${uiState.proposals.size}건", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextMain)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.toggleSortOrder() }
                    ) {
                        Text(
                            text = if (uiState.ascending) "최저가 순" else "최고가 순",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .size(14.dp)
                                .rotate(if (uiState.ascending) 0f else 180f)
                        )
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.proposals) { proposal ->
                        ProposalCard(
                            proposal = proposal,
                            isLowestPrice = proposal.price == lowestPrice,
                            onRequestEstimate = { onRequestEstimate(proposal.shop.shopId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProposalCard(
    proposal: DesignProposal,
    isLowestPrice: Boolean,
    onRequestEstimate: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (isLowestPrice) {
            Text(
                text = "최저가",
                style = MaterialTheme.typography.labelSmall,
                color = MutedRosePrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .border(1.dp, MutedRosePrimary, RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = proposal.shop.imageUrl,
                contentDescription = proposal.shop.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BorderLight)
            )
            Column(modifier = Modifier.padding(start = 10.dp).weight(1f)) {
                Text(text = proposal.shop.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                if (proposal.shop.rating != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = MutedRosePrimary, modifier = Modifier.size(12.dp))
                        Text(
                            text = " ${proposal.shop.rating} (${proposal.shop.reviewCount ?: 0}) · ${proposal.shop.address}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                } else {
                    Text(text = proposal.shop.address, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
            Text(
                text = "${"%,d".format(proposal.price)}원",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                fontSize = 17.sp
            )
        }

        if (proposal.workPhotoUrls.orEmpty().isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                proposal.workPhotoUrls.orEmpty().take(3).forEach { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }

        OutlinedButton(
            onClick = onRequestEstimate,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMain),
            border = BorderStroke(1.dp, BorderLight),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(48.dp)
        ) {
            Text(text = "견적받기", fontWeight = FontWeight.Bold)
        }
    }
}
