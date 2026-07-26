package com.example.nailnail.ui.main.estimate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nailnail.ui.common.PhotoPlaceholder
import com.example.nailnail.ui.main.estimate.components.PriceBreakdownView
import com.example.nailnail.ui.main.estimate.components.ReviewCard
import com.example.nailnail.ui.theme.AppBackground
import com.example.nailnail.ui.theme.DividerGray
import com.example.nailnail.ui.theme.MutedRoseBgLight
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.SurfaceWhite
import com.example.nailnail.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailScreen(shopId: String, onBackClick: () -> Unit, onReserveClick: (String) -> Unit) {
    val shop = remember(shopId) { MockShopDetails.find { it.id == shopId } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("상세보기") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        if (shop == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("샵 정보를 찾을 수 없어요", color = TextSecondary)
            }
            return@Scaffold
        }

        var selectedSlot by remember { mutableStateOf<Int?>(null) }
        var breakdownExpanded by remember { mutableStateOf(true) }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item { PhotoPlaceholder(modifier = Modifier.fillMaxWidth().height(200.dp), cornerRadius = 0.dp) }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = shop.shopName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${shop.rating} · ${shop.distance}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "📍 ${shop.address}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Text(
                        text = "🕒 ${shop.closedDays}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AppBackground)
                            .padding(16.dp)
                    ) {
                        Text(text = "샵 코멘트", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text(
                            text = shop.comment,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            item { SectionDivider() }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "예약 가능 시간", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    shop.timeSlots.chunked(2).forEachIndexed { rowIndex, rowSlots ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowSlots.forEachIndexed { colIndex, slot ->
                                val index = rowIndex * 2 + colIndex
                                val selected = index == selectedSlot
                                Text(
                                    text = slot,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selected) MutedRosePrimary else MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (selected) MutedRoseBgLight else SurfaceWhite)
                                        .border(
                                            1.dp,
                                            if (selected) MutedRosePrimary else DividerGray,
                                            RoundedCornerShape(6.dp)
                                        )
                                        .clickable { selectedSlot = index }
                                        .padding(vertical = 12.dp)
                                )
                            }
                        }
                    }
                    Button(
                        onClick = { selectedSlot?.let { onReserveClick(shop.id) } },
                        enabled = selectedSlot != null,
                        colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(52.dp)
                    ) {
                        Text("예약하기")
                    }
                }
            }

            item { SectionDivider() }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "견적서", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(3) { PhotoPlaceholder(modifier = Modifier.size(80.dp)) }
                    }
                    Text(
                        text = shop.styleKeywords,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 20.dp)
                    )
                    PriceBreakdownView(
                        sections = shop.priceSections,
                        finalPrice = shop.finalPrice,
                        expanded = breakdownExpanded
                    )
                    Text(
                        text = if (breakdownExpanded) "간략히 ∧" else "자세히 ∨",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { breakdownExpanded = !breakdownExpanded }
                            .padding(vertical = 16.dp)
                    )
                }
            }

            item { SectionDivider() }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "디자인 매칭", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(
                        text = "요청하신 디자인과의 유사도 ${shop.designMatchPercent}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MutedRosePrimary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(3) { PhotoPlaceholder(modifier = Modifier.size(100.dp)) }
                    }
                }
            }

            item { SectionDivider() }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "최근 리뷰",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        shop.reviews.forEach { review -> ReviewCard(review = review) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(AppBackground)
    )
}
