package com.example.nailnail.ui.main.estimate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnail.ui.common.PhotoPlaceholder
import com.example.nailnail.ui.main.estimate.components.ShopEstimateCard
import com.example.nailnail.ui.theme.DividerGray
import com.example.nailnail.ui.theme.MutedRoseBgLight
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.SurfaceWhite
import com.example.nailnail.ui.theme.TextSecondary

private val DateFilters = listOf("전체", "7/4 (토)", "7/5 (일)")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimateComparisonScreen(
    estimateId: String,
    onBackClick: () -> Unit,
    onShopDetailClick: (String) -> Unit,
    onReserveClick: (String) -> Unit
) {
    val estimate = remember(estimateId) { MockEstimateItems.find { it.id == estimateId } }
    var selectedDateFilter by remember { mutableIntStateOf(0) }

    Scaffold(
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                if (estimate != null) {
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
                            Text(text = estimate.styleKeywords, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            Row(modifier = Modifier.padding(top = 6.dp)) {
                                Text(
                                    text = estimate.status,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MutedRosePrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = " · ${estimate.replyStatus}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DateFilters.forEachIndexed { index, label ->
                        val selected = index == selectedDateFilter
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected) MutedRosePrimary else TextSecondary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selected) MutedRoseBgLight else SurfaceWhite)
                                .border(
                                    1.dp,
                                    if (selected) MutedRosePrimary else DividerGray,
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "최저가 ∨",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(MockShopEstimates, key = { it.id }) { shop ->
                ShopEstimateCard(
                    shop = shop,
                    onDetailClick = { onShopDetailClick(shop.id) },
                    onReserveClick = { onReserveClick(shop.id) }
                )
            }
        }
    }
}
