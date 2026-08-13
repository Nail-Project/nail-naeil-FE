package com.example.nailnaeil.ui.main.estimate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.data.remote.dto.EstimateResultResponseItem
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextDisabled
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun ShopEstimateCard(
    response: EstimateResultResponseItem,
    onDetailClick: () -> Unit,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (response.isLowestPrice) {
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
                text = if (response.canProvideService) {
                    response.totalPrice?.let { "%,d원".format(it) } ?: "가격 문의"
                } else {
                    "시술 불가"
                },
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
            Text(text = response.shop.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (response.isRemovalIncluded) {
                Text(text = "제거비 포함", style = MaterialTheme.typography.labelSmall, color = TextDisabled)
            }
        }

        Text(
            text = listOfNotNull(
                "★ ${"%.1f".format(response.shop.rating)} (${response.shop.reviewCount})",
                response.distanceMeters?.let { "내 위치에서 ${"%.1f".format(it / 1000.0)}km" }
            ).joinToString(" · "),
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "예상 시술 시간 ${response.estimatedDurationMinutes}분",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp)
        )

        if (response.canProvideService && response.proposalDateTimes.isNotEmpty()) {
            Text(
                text = "예약 가능 시간 ${response.proposalDateTimes.size}개",
                style = MaterialTheme.typography.bodySmall,
                color = MutedRosePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
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
                onClick = onDetailClick,
                enabled = response.canProvideService,
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
