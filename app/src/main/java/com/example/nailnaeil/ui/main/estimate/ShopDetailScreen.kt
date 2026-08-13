package com.example.nailnaeil.ui.main.estimate

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nailnaeil.ui.common.PhotoPlaceholder
import com.example.nailnaeil.ui.main.estimate.components.PriceBreakdownView
import com.example.nailnaeil.ui.main.estimate.components.ReviewCard
import com.example.nailnaeil.ui.main.reservation.components.ReservationConfirmDialog
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailScreen(
    shopId: String,
    onBackClick: () -> Unit,
    onReservationConfirmed: () -> Unit,
    viewModel: ShopDetailViewModel = viewModel { ShopDetailViewModel(shopId.toLongOrNull() ?: 0L) }
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val proposal = uiState.proposal
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.reservationConfirmed) {
        if (uiState.reservationConfirmed) onReservationConfirmed()
    }

    LaunchedEffect(uiState.reserveError) {
        uiState.reserveError?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

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
        when {
            uiState.isLoading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            proposal == null -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { Text(uiState.errorMessage ?: "샵 정보를 찾을 수 없어요", color = TextSecondary) }

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    if (uiState.thumbnailImageUrl != null) {
                        AsyncImage(
                            model = uiState.thumbnailImageUrl,
                            contentDescription = proposal.shop.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                    } else {
                        PhotoPlaceholder(modifier = Modifier.fillMaxWidth().height(220.dp), cornerRadius = 0.dp)
                    }
                }

                item {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = proposal.shop.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(
                            text = "★ ${"%.1f".format(proposal.shop.rating)} (${proposal.shop.reviewCount})",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "📍 ${proposal.shop.address}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                        val closedDays = proposal.shop.closedDays
                        if (!closedDays.isNullOrEmpty()) {
                            Text(
                                text = "🕒 ${closedDays.joinToString(", ")} 휴무",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        if (!proposal.memo.isNullOrBlank()) {
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
                                    text = proposal.memo,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                if (proposal.canProvideService) {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                            Text(text = "예약 가능 시간", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            uiState.proposalTimes.chunked(2).forEach { rowSlots ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowSlots.forEach { slot ->
                                        val selected = slot.id == uiState.selectedTimeId
                                        Text(
                                            text = slot.proposalDateTime.replace("T", "\n").take(16),
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
                                                .clickable { viewModel.selectTime(slot.id) }
                                                .padding(vertical = 12.dp)
                                        )
                                    }
                                    if (rowSlots.size == 1) {
                                        Box(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                            Button(
                                onClick = { showConfirmDialog = true },
                                enabled = uiState.selectedTimeId != null && !uiState.isReserving,
                                colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .height(52.dp)
                            ) {
                                Text(if (uiState.isReserving) "예약 중..." else "예약하기")
                            }
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                        Text(text = "견적서", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        val price = proposal.price
                        val sections = listOfNotNull(
                            price.basePrice?.let { PriceBreakdownSection("기본 가격", listOf(PriceBreakdownRow("기본 시술", it))) },
                            price.designExtraPrice?.let { PriceBreakdownSection("디자인 추가", listOf(PriceBreakdownRow("디자인 추가금", it))) },
                            price.optionExtraPrice?.let { PriceBreakdownSection("옵션 추가", listOf(PriceBreakdownRow("옵션 추가금", it))) },
                            price.removalPrice?.let { PriceBreakdownSection("제거 비용", listOf(PriceBreakdownRow("기존 제거", it))) }
                        )
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            if (sections.isNotEmpty()) {
                                PriceBreakdownView(sections = sections, finalPrice = price.totalPrice ?: 0)
                            } else {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "총 금액", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = price.totalPrice?.let { "%,d원".format(it) } ?: "시술 불가",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MutedRosePrimary
                                    )
                                }
                            }
                        }
                    }
                }

                if (uiState.recentReviews.isNotEmpty()) {
                    item {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "최근 리뷰",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                uiState.recentReviews.forEach { review -> ReviewCard(review = review) }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showConfirmDialog && proposal != null) {
        val selectedTime = uiState.proposalTimes.find { it.id == uiState.selectedTimeId }
        Dialog(onDismissRequest = { showConfirmDialog = false }) {
            ReservationConfirmDialog(
                shopName = proposal.shop.name,
                dateTime = selectedTime?.proposalDateTime?.replace("T", " · ").orEmpty(),
                priceText = proposal.price.totalPrice?.let { "%,d원".format(it) }.orEmpty(),
                onDismiss = { showConfirmDialog = false },
                onConfirm = {
                    showConfirmDialog = false
                    viewModel.reserve()
                }
            )
        }
    }
}
