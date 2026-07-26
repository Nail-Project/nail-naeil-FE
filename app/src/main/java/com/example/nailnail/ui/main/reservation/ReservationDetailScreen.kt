package com.example.nailnail.ui.main.reservation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.nailnail.ui.main.reservation.components.ReservationCancelConfirmSheet
import com.example.nailnail.ui.main.reservation.components.ReservationCancelReasonSheet
import com.example.nailnail.ui.main.reservation.components.ReservationChangeDialog
import com.example.nailnail.ui.theme.DangerBgLight
import com.example.nailnail.ui.theme.DangerRed
import com.example.nailnail.ui.theme.DividerGray
import com.example.nailnail.ui.theme.MutedRoseBgLight
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.SurfaceWhite
import com.example.nailnail.ui.theme.TextSecondary

private enum class ReservationSheet { NONE, CANCEL_CONFIRM, CANCEL_REASON }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDetailScreen(reservationId: String, onBackClick: () -> Unit, onCancelled: () -> Unit) {
    val reservation = remember(reservationId) { ReservationMockState.findById(reservationId) }
    var activeSheet by remember { mutableStateOf(ReservationSheet.NONE) }
    var showChangeDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        containerColor = SurfaceWhite,
        topBar = {
            TopAppBar(
                title = { Text("확정된 예약") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        },
        bottomBar = {
            if (reservation != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { activeSheet = ReservationSheet.CANCEL_CONFIRM },
                        colors = ButtonDefaults.buttonColors(containerColor = DangerBgLight, contentColor = DangerRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(56.dp)
                    ) {
                        Text("예약 취소", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { showChangeDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(56.dp)
                    ) {
                        Text("예약 변경", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        if (reservation == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("예약 정보를 찾을 수 없어요", color = TextSecondary)
            }
            return@Scaffold
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MutedRoseBgLight)
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "시술예정",
                        style = MaterialTheme.typography.labelMedium,
                        color = SurfaceWhite,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(MutedRosePrimary)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                    Text(
                        text = reservation.dateTime,
                        style = MaterialTheme.typography.titleMedium,
                        color = MutedRosePrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = reservation.shopName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.padding(top = 10.dp)) {
                        Text(
                            text = "${reservation.rating} · ${reservation.distance}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 17.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = MutedRosePrimary, modifier = Modifier.height(20.dp))
                            Text(
                                text = reservation.address,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Map, contentDescription = "지도에서 위치 확인", tint = MutedRosePrimary, modifier = Modifier.height(22.dp))
                            Text(
                                text = "위치",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MutedRosePrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Schedule, contentDescription = null, modifier = Modifier.height(20.dp))
                        Text(
                            text = reservation.closedDays,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp)
                    ) {
                        Text(text = "샵 코멘트", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
                        Text(
                            text = reservation.comment,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 13.dp)
                        )
                    }
                }
            }

            item { HorizontalDivider(color = DividerGray, modifier = Modifier.padding(horizontal = 16.dp)) }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "견적서", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceWhite)
                            .padding(16.dp)
                    ) {
                        PriceRow("기본 가격", reservation.basePrice)
                        PriceRow("디자인 추가", reservation.designPrice)
                        PriceRow("옵션 추가 (1)", reservation.optionPrice)
                        PriceRow("쿠폰 할인", -reservation.couponDiscount)

                        HorizontalDivider(color = DividerGray, modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "최종 예상 금액", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(
                                text = "%,d원".format(reservation.finalPrice),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MutedRosePrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    if (activeSheet != ReservationSheet.NONE && reservation != null) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(onDismissRequest = { activeSheet = ReservationSheet.NONE }, sheetState = sheetState) {
            when (activeSheet) {
                ReservationSheet.CANCEL_CONFIRM -> ReservationCancelConfirmSheet(
                    reservation = reservation,
                    onKeepReservation = { activeSheet = ReservationSheet.NONE },
                    onConfirmCancel = { activeSheet = ReservationSheet.CANCEL_REASON }
                )

                ReservationSheet.CANCEL_REASON -> ReservationCancelReasonSheet(
                    onBack = { activeSheet = ReservationSheet.NONE },
                    onComplete = {
                        ReservationMockState.cancel(reservation.id)
                        activeSheet = ReservationSheet.NONE
                        Toast.makeText(context, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                        onCancelled()
                    }
                )

                ReservationSheet.NONE -> Unit
            }
        }
    }

    if (showChangeDialog) {
        Dialog(onDismissRequest = { showChangeDialog = false }) {
            ReservationChangeDialog(onDismiss = { showChangeDialog = false })
        }
    }
}

@Composable
private fun PriceRow(label: String, amount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
        Text(
            text = if (amount < 0) "-%,d원".format(-amount) else "%,d원".format(amount),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
