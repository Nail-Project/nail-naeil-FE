package com.example.nailnaeil.ui.main.estimate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.main.estimate.ShopEstimate
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextDisabled
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun ShopEstimateCard(
    shop: ShopEstimate,
    onDetailClick: () -> Unit,
    onReserveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceWhite)
            .border(1.dp, DividerGray, RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        if (shop.isWaiting) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = shop.averageResponseTime.orEmpty(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MutedRosePrimary,
                    fontWeight = FontWeight.Bold
                )
                if (shop.removalIncluded) {
                    Text(text = "제거비 포함", style = MaterialTheme.typography.labelSmall, color = TextDisabled)
                }
            }
            Row(
                modifier = Modifier.padding(top = 12.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = shop.shopName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (shop.rating != null) {
                    Text(
                        text = shop.rating,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            OutlinedButton(
                onClick = {},
                enabled = false,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(48.dp)
            ) {
                Text("응답 대기중")
            }
        } else {
            var selectedSlot by remember { mutableIntStateOf(0) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (shop.isLowestPrice) {
                    Text(
                        text = "최저가",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedRosePrimary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MutedRoseBgLight)
                            .border(1.dp, MutedRosePrimary, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                } else {
                    Spacer(modifier = Modifier)
                }
                Text(
                    text = shop.priceText.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = shop.shopName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (shop.removalIncluded) {
                    Text(text = "제거비 포함", style = MaterialTheme.typography.labelSmall, color = TextDisabled)
                }
            }

            Text(
                text = listOfNotNull(shop.rating, shop.distance).joinToString(" · "),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )

            shop.comment?.let { comment ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MutedRoseBgLight)
                        .padding(12.dp)
                ) {
                    Text(text = "샵 코멘트", style = MaterialTheme.typography.labelSmall, color = TextDisabled)
                    Text(
                        text = comment,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            if (shop.timeSlots.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    shop.timeSlots.forEachIndexed { index, slot ->
                        val selected = index == selectedSlot
                        Text(
                            text = slot,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected) MutedRosePrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selected) MutedRoseBgLight else SurfaceWhite)
                                .border(
                                    1.dp,
                                    if (selected) MutedRosePrimary else DividerGray,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { selectedSlot = index }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDetailClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("상세보기")
                }
                Button(
                    onClick = {
                        val dateTime = shop.timeSlots.getOrNull(selectedSlot).orEmpty()
                        onReserveClick(dateTime)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("예약하기")
                }
            }
        }
    }
}
