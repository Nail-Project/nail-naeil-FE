package com.example.nailnail.ui.main.home.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnail.ui.common.PhotoPlaceholder
import com.example.nailnail.ui.main.home.EstimateSummary
import com.example.nailnail.ui.theme.AppBackground
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.TextSecondary

@Composable
fun InProgressEstimateSection(
    estimates: List<EstimateSummary>,
    onSeeAllClick: () -> Unit,
    onEstimateClick: (EstimateSummary) -> Unit,
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
private fun EstimateCard(estimate: EstimateSummary, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PhotoPlaceholder(modifier = Modifier.size(64.dp))
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = estimate.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(
                text = "${estimate.totalCount}개 업체중 ${estimate.respondedCount}개 응답 완료",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Text(
                text = "최저 ${"%,d".format(estimate.minPrice)}원",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MutedRosePrimary
            )
        }
    }
}
