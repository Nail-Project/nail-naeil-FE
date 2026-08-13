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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.DesignProposal
import com.example.nailnaeil.data.remote.dto.DesignSummary
import com.example.nailnaeil.data.remote.dto.difficultyLabel
import com.example.nailnaeil.data.remote.dto.formatDurationMinutes
import com.example.nailnaeil.data.remote.dto.shapeLabel
import com.example.nailnaeil.ui.theme.BorderLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun DesignDetailScreen(
    designId: Long,
    onBackClick: () -> Unit,
    onStartEstimate: () -> Unit,
    onProposalClick: (Long) -> Unit = {},
    onSimilarDesignClick: (Long) -> Unit = {},
    onSeeAllProposalsClick: (Long) -> Unit = {},
    onSeeAllSimilarDesignsClick: (Long) -> Unit = {},
    viewModel: DesignDetailViewModel = viewModel { DesignDetailViewModel(designId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val design = uiState.design

    Box(modifier = Modifier.fillMaxSize().background(SurfaceWhite)) {
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
                    .padding(bottom = 24.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                    AsyncImage(
                        model = design.imageUrl,
                        contentDescription = design.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "뒤로가기",
                        tint = SurfaceWhite,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                            .size(20.dp)
                            .clickable(onClick = onBackClick)
                    )
                    Icon(
                        imageVector = if (design.isBookmarked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "찜하기",
                        tint = SurfaceWhite,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(24.dp)
                            .clickable { viewModel.toggleWish() }
                    )
                }

                Column(modifier = Modifier.padding(top = 20.dp)) {
                    Text(
                        text = design.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = " ${design.viewCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = " ${design.wishCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    if (design.tags.orEmpty().isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp, start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                        ) {
                            design.tags.orEmpty().forEach { tag ->
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedRosePrimary,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(MutedRosePrimary.copy(alpha = 0.12f))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    EstimatedPriceSection(
                        priceMin = design.estimatedPriceMin,
                        priceMax = design.estimatedPriceMax,
                        averagePrice = design.averageEstimatePrice,
                        modifier = Modifier.padding(top = 24.dp, start = 20.dp, end = 20.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                            .height(1.dp)
                            .background(BorderLight)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        DetailStatColumn(
                            icon = Icons.Filled.AccessTime,
                            label = "시술 시간",
                            value = formatDurationMinutes(design.durationMinutes) ?: "-"
                        )
                        DetailStatColumn(
                            icon = Icons.Filled.SignalCellularAlt,
                            label = "난이도",
                            value = difficultyLabel(design.difficulty) ?: "-"
                        )
                        DetailStatColumn(
                            icon = Icons.Filled.Spa,
                            label = "추천 쉐입",
                            value = shapeLabel(design.recommendedShape) ?: "-"
                        )
                    }

                    Text(
                        text = "디자인 설명",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 32.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
                    )
                    Text(
                        text = design.description?.takeUnless { it.isBlank() } ?: "아직 등록된 설명이 없어요.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (design.description.isNullOrBlank()) TextSecondary else TextMain,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                    )

                    SectionHeader(
                        title = "최근 견적 사례",
                        onSeeAllClick = { onSeeAllProposalsClick(designId) },
                        modifier = Modifier.padding(top = 32.dp, start = 20.dp, end = 20.dp)
                    )
                    if (design.recentProposals.orEmpty().isEmpty()) {
                        EmptySectionPlaceholder(text = "아직 견적 사례가 없어요.")
                    } else {
                        LazyRow(
                            modifier = Modifier.padding(top = 12.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(design.recentProposals.orEmpty()) { proposal ->
                                RecentProposalCard(proposal = proposal, onClick = { onProposalClick(proposal.proposalId) })
                            }
                        }
                    }

                    SectionHeader(
                        title = "비슷한 디자인",
                        onSeeAllClick = { onSeeAllSimilarDesignsClick(designId) },
                        modifier = Modifier.padding(top = 32.dp, start = 20.dp, end = 20.dp)
                    )
                    if (design.similarDesigns.orEmpty().isEmpty()) {
                        EmptySectionPlaceholder(text = "비슷한 디자인을 준비 중이에요.")
                    } else {
                        LazyRow(
                            modifier = Modifier.padding(top = 12.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(design.similarDesigns.orEmpty()) { similar ->
                                SimilarDesignCard(design = similar, onClick = { onSimilarDesignClick(similar.designId) })
                            }
                        }
                    }

                    Button(
                        onClick = onStartEstimate,
                        colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, start = 20.dp, end = 20.dp)
                            .height(60.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "이 디자인 그대로", color = SurfaceWhite, fontSize = 12.sp)
                            Text(text = "디자인 견적받기", color = SurfaceWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EstimatedPriceSection(
    priceMin: Int?,
    priceMax: Int?,
    averagePrice: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "예상 시술가", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Icon(
                    imageVector = Icons.Filled.HelpOutline,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.padding(start = 4.dp).size(14.dp)
                )
            }
            Text(
                text = if (priceMin != null && priceMax != null) {
                    "${"%,d".format(priceMin)}원 ~ ${"%,d".format(priceMax)}원"
                } else {
                    "-"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "최근 평균 견적가", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Text(
                text = averagePrice?.let { "${"%,d".format(it)}원" } ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun DetailStatColumn(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Icon(imageVector = icon, contentDescription = null, tint = MutedRosePrimary, modifier = Modifier.size(20.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            modifier = Modifier.padding(top = 6.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = TextMain,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun SectionHeader(title: String, onSeeAllClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onSeeAllClick)
        ) {
            Text(text = "전체보기", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun EmptySectionPlaceholder(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 20.dp, end = 20.dp)
    )
}

@Composable
private fun RecentProposalCard(proposal: DesignProposal, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .clickable(onClick = onClick)
            .padding(12.dp),
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
            Text(text = proposal.shop.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = proposal.shop.address, style = MaterialTheme.typography.bodySmall, color = TextSecondary, maxLines = 1)
        }
        Text(
            text = "${"%,d".format(proposal.price)}원",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MutedRosePrimary
        )
    }
}

@Composable
private fun SimilarDesignCard(design: DesignSummary, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = design.imageUrl,
            contentDescription = design.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xB3000000))
                    )
                )
                .padding(8.dp)
        ) {
            Text(
                text = design.title,
                style = MaterialTheme.typography.labelSmall,
                color = SurfaceWhite,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}
