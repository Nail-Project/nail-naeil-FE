package com.example.nailnaeil.ui.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.EstimateListItem
import com.example.nailnaeil.ui.common.PhotoPlaceholder
import com.example.nailnaeil.ui.theme.AppBackground
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun InProgressEstimateSection(
    estimates: List<EstimateListItem>,
    onSeeAllClick: () -> Unit,
    onEstimateClick: (EstimateListItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "진행 중인 견적",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (estimates.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onSeeAllClick)
                ) {
                    Text(text = "전체보기", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        if (estimates.isEmpty()) {
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppBackground)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = TextSecondary)
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(text = "요청한 견적이 없어요.", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "지금 바로 견적을 요청해보세요!",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                estimates.forEach { estimate ->
                    EstimateCard(estimate = estimate, onClick = { onEstimateClick(estimate) })
                }
            }
        }
    }
}

@Composable
private fun EstimateCard(estimate: EstimateListItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val thumbnailUrl = estimate.images.firstOrNull()?.imageUrl
        if (thumbnailUrl != null) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = estimate.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            PhotoPlaceholder(modifier = Modifier.size(64.dp))
        }
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = estimate.title ?: "견적 요청",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${estimate.submittedShopCount}개 업체 응답 · 견적 ${estimate.proposalCount}건 도착",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            if (estimate.minPrice != null) {
                Text(
                    text = "최저 ${"%,d".format(estimate.minPrice)}원",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MutedRosePrimary
                )
            }
        }
    }
}
