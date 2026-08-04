package com.example.nailnaeil.ui.main.estimate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.window.Dialog
import com.example.nailnaeil.ui.common.PhotoPlaceholder
import com.example.nailnaeil.ui.main.estimate.components.PriceBreakdownView
import com.example.nailnaeil.ui.main.estimate.components.ReviewCard
import com.example.nailnaeil.ui.main.reservation.Reservation
import com.example.nailnaeil.ui.main.reservation.ReservationStatus
import com.example.nailnaeil.ui.main.reservation.components.ReservationConfirmDialog
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary
import com.example.nailnaeil.R

private const val CoverPageCount = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailScreen(
    shopId: String,
    onBackClick: () -> Unit,
    onReservationConfirmed: (Reservation) -> Unit
) {
    val shop = remember(shopId) { MockShopDetails.find { it.id == shopId } }

    Scaffold(
        containerColor = SurfaceWhite,
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
        var breakdownExpanded by remember { mutableStateOf(false) }
        var showConfirmDialog by remember { mutableStateOf(false) }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                val pagerState = rememberPagerState(pageCount = { CoverPageCount })
                Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
                        PhotoPlaceholder(modifier = Modifier.fillMaxSize(), cornerRadius = 0.dp)
                    }
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(CoverPageCount) { index ->
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (index == pagerState.currentPage) SurfaceWhite else SurfaceWhite.copy(alpha = 0.5f)
                                    )
                            )
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = shop.shopName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${shop.rating} · ${shop.distance}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📍 ${shop.address}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            contentDescription = "주소 펼치기",
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "🗺 위치",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MutedRosePrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Text(
                        text = "🕒 ${shop.closedDays}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MutedRoseBgLight)
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

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
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
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selected) MutedRoseBgLight else SurfaceWhite)
                                        .border(
                                            1.dp,
                                            if (selected) MutedRosePrimary else DividerGray,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedSlot = index }
                                        .padding(vertical = 12.dp)
                                )
                            }
                        }
                    }
                    Button(
                        onClick = { showConfirmDialog = true },
                        enabled = selectedSlot != null,
                        colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(52.dp)
                    ) {
                        Text("예약하기")
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                    Text(text = "견적서", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                    if (breakdownExpanded) {
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
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    PriceBreakdownView(
                        sections = shop.priceSections,
                        finalPrice = shop.finalPrice,
                        expanded = breakdownExpanded
                    )
                    Text(
                        text = if (breakdownExpanded) "간략히 ∧" else "더보기 ∨",
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

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
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

        if (showConfirmDialog) {
            val slotIndex = selectedSlot
            if (slotIndex != null) {
                val dateTime = shop.timeSlots[slotIndex].replace("\n", " · ")
                val priceText = "%,d원".format(shop.finalPrice)
                Dialog(onDismissRequest = { showConfirmDialog = false }) {
                    ReservationConfirmDialog(
                        shopName = shop.shopName,
                        dateTime = dateTime,
                        priceText = priceText,
                        onDismiss = { showConfirmDialog = false },
                        onConfirm = {
                            showConfirmDialog = false
                            onReservationConfirmed(
                                Reservation(
                                    id = "r${System.currentTimeMillis()}",
                                    status = ReservationStatus.CONFIRMED,
                                    dateTime = dateTime,
                                    shopName = shop.shopName,
                                    design = shop.styleKeywords,
                                    option = "",
                                    price = shop.finalPrice,
                                    imageRes = R.drawable.img_nail_1,
                                    rating = shop.rating,
                                    distance = shop.distance,
                                    address = shop.address,
                                    closedDays = shop.closedDays,
                                    comment = shop.comment,
                                    basePrice = shop.priceSections.find { it.title == "기본 가격" }?.totalAmount ?: 0,
                                    designPrice = shop.priceSections.find { it.title == "디자인 추가" }?.totalAmount ?: 0,
                                    optionPrice = shop.priceSections.find { it.title == "옵션 추가" }?.totalAmount ?: 0,
                                    couponDiscount = 0
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
