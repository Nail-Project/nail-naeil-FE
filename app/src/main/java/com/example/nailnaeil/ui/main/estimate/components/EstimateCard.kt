package com.example.nailnaeil.ui.main.estimate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.EstimateListItem
import com.example.nailnaeil.ui.common.PhotoPlaceholder
import com.example.nailnaeil.ui.main.estimate.estimateStatusLabel
import com.example.nailnaeil.ui.main.estimate.label
import com.example.nailnaeil.ui.main.estimate.labels
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextDisabled
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun EstimateCard(item: EstimateListItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val isCompleted = item.status == "COMPLETED" || item.status == "EXPIRED"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite)
            .border(1.dp, DividerGray, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = estimateStatusLabel(item.status, item.submittedShopCount),
                style = MaterialTheme.typography.labelSmall,
                color = MutedRosePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MutedRoseBgLight)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = "제안 ${item.proposalCount}건 도착",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedRosePrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Row(modifier = Modifier.padding(top = 12.dp)) {
            val thumbnailUrl = item.images.firstOrNull()?.imageUrl
            if (thumbnailUrl != null) {
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                PhotoPlaceholder(modifier = Modifier.size(72.dp))
            }
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(
                    text = item.title ?: item.nailType.label(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${item.nailType.label()} · ${item.removalTypes.labels()}" +
                        (item.radiusMeters?.let { " · 근처 ${it / 1000}km" } ?: ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
                val firstSchedule = item.schedules.firstOrNull()
                if (firstSchedule != null) {
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        Text(text = firstSchedule.date, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        if (item.schedules.size > 1) {
                            Text(
                                text = " 외 ${item.schedules.size - 1}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextDisabled,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        if (item.lowestPriceShop != null) {
            HorizontalDivider(modifier = Modifier.padding(top = 16.dp), color = DividerGray)
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "최저가", style = MaterialTheme.typography.labelSmall, color = MutedRosePrimary, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = item.lowestPriceShop.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text(text = item.lowestPriceShop.address, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Text(
                        text = item.minPrice?.let { "%,d원".format(it) }.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        OutlinedButton(
            onClick = onClick,
            enabled = !isCompleted,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(48.dp)
        ) {
            Text(if (isCompleted) "마감된 견적입니다" else "자세히 보기")
        }
    }
}
