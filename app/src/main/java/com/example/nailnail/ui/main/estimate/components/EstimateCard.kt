package com.example.nailnail.ui.main.estimate.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnail.ui.common.PhotoPlaceholder
import com.example.nailnail.ui.main.estimate.EstimateItem
import com.example.nailnail.ui.theme.DividerGray
import com.example.nailnail.ui.theme.MutedRoseBgLight
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.SurfaceWhite
import com.example.nailnail.ui.theme.TextDisabled
import com.example.nailnail.ui.theme.TextSecondary

@Composable
fun EstimateCard(item: EstimateItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
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
                text = item.status,
                style = MaterialTheme.typography.labelSmall,
                color = MutedRosePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MutedRoseBgLight)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = item.replyStatus,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedRosePrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Row(modifier = Modifier.padding(top = 12.dp)) {
            PhotoPlaceholder(modifier = Modifier.size(72.dp))
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(text = item.styleKeywords, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(
                    text = item.options,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(text = item.dateTime, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = item.dateExtra,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDisabled,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }

        if (item.hasLowestPrice) {
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
                        Text(text = item.shopName.orEmpty(), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text(text = item.shopLocation.orEmpty(), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Text(text = item.priceText.orEmpty(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }

        OutlinedButton(
            onClick = onClick,
            enabled = !item.isCompleted,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(48.dp)
        ) {
            Text(if (item.isCompleted) "시술이 완료된 견적입니다" else "자세히 보기")
        }
    }
}
