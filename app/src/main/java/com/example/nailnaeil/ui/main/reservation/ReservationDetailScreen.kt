package com.example.nailnaeil.ui.main.reservation

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.nailnaeil.R
import com.example.nailnaeil.data.remote.dto.ReservationDetailResponse
import com.example.nailnaeil.data.repository.ReservationRepository
import com.example.nailnaeil.ui.main.reservation.components.ReservationCancelConfirmSheet
import com.example.nailnaeil.ui.main.reservation.components.ReservationCancelReasonSheet
import com.example.nailnaeil.ui.main.reservation.components.ReservationChangeDialog
import com.example.nailnaeil.ui.theme.DangerBgLight
import com.example.nailnaeil.ui.theme.DangerRed
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private enum class ReservationSheet {
    NONE,
    CANCEL_CONFIRM,
    CANCEL_REASON
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDetailScreen(
    reservationId: String,
    reservationRepository: ReservationRepository,
    onBackClick: () -> Unit,
    onCancelled: () -> Unit
) {
    var reservation by remember(
        reservationId
    ) {
        mutableStateOf<Reservation?>(null)
    }

    var isLoading by remember(
        reservationId
    ) {
        mutableStateOf(true)
    }

    var errorMessage by remember(
        reservationId
    ) {
        mutableStateOf<String?>(null)
    }

    var retryCount by remember {
        mutableStateOf(0)
    }

    var activeSheet by remember {
        mutableStateOf(ReservationSheet.NONE)
    }

    var showChangeDialog by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    LaunchedEffect(
        reservationId,
        retryCount
    ) {
        isLoading = true
        errorMessage = null

        val parsedReservationId =
            reservationId.toLongOrNull()

        if (parsedReservationId == null) {
            isLoading = false
            errorMessage =
                "유효하지 않은 예약 id입니다."
            return@LaunchedEffect
        }

        reservationRepository
            .getReservationDetail(
                reservationId =
                    parsedReservationId
            )
            .onSuccess { response ->
                reservation =
                    response.toReservation()
            }
            .onFailure { exception ->
                reservation = null
                errorMessage =
                    exception.message
                        ?: "예약 정보를 불러오지 못했습니다."
            }

        isLoading = false
    }

    Scaffold(
        containerColor = SurfaceWhite,

        topBar = {
            TopAppBar(
                title = {
                    Text("확정된 예약")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector =
                                Icons.Filled.ArrowBack,
                            contentDescription =
                                "뒤로가기"
                        )
                    }
                }
            )
        },

        bottomBar = {
            if (reservation != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceWhite)
                        .padding(16.dp),
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            activeSheet =
                                ReservationSheet
                                    .CANCEL_CONFIRM
                        },
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        DangerBgLight,
                                    contentColor =
                                        DangerRed
                                ),
                        shape =
                            RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        Text(
                            text = "예약 취소",
                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            showChangeDialog = true
                        },
                        shape =
                            RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        Text(
                            text = "예약 변경",
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment =
                        Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MutedRosePrimary
                    )
                }
            }

            errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {
                    Text(
                        text = errorMessage
                            ?: "오류가 발생했습니다.",
                        style =
                            MaterialTheme
                                .typography
                                .bodyLarge,
                        color = TextSecondary
                    )

                    Button(
                        onClick = {
                            retryCount += 1
                        },
                        modifier =
                            Modifier.padding(
                                top = 20.dp
                            ),
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        MutedRosePrimary
                                )
                    ) {
                        Text("다시 시도")
                    }
                }
            }

            reservation != null -> {
                ReservationDetailContent(
                    reservation =
                        requireNotNull(
                            reservation
                        ),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            }
        }
    }

    val currentReservation =
        reservation

    if (
        activeSheet !=
        ReservationSheet.NONE &&
        currentReservation != null
    ) {
        val sheetState =
            rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = {
                activeSheet =
                    ReservationSheet.NONE
            },
            sheetState = sheetState,
            containerColor = SurfaceWhite
        ) {
            when (activeSheet) {
                ReservationSheet
                    .CANCEL_CONFIRM -> {

                    ReservationCancelConfirmSheet(
                        reservation =
                            currentReservation,
                        onKeepReservation = {
                            activeSheet =
                                ReservationSheet.NONE
                        },
                        onConfirmCancel = {
                            activeSheet =
                                ReservationSheet
                                    .CANCEL_REASON
                        }
                    )
                }

                ReservationSheet
                    .CANCEL_REASON -> {

                    ReservationCancelReasonSheet(
                        onBack = {
                            activeSheet =
                                ReservationSheet.NONE
                        },
                        onComplete = {
                            /*
                             * 예약 취소 API는 현재
                             * 담당 범위가 아니므로
                             * 기존 로컬 동작만 유지한다.
                             */
                            ReservationMockState
                                .cancel(
                                    currentReservation.id
                                )

                            activeSheet =
                                ReservationSheet.NONE

                            Toast.makeText(
                                context,
                                "예약 취소 API는 아직 연결되지 않았습니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                            onCancelled()
                        }
                    )
                }

                ReservationSheet.NONE ->
                    Unit
            }
        }
    }

    if (showChangeDialog) {
        Dialog(
            onDismissRequest = {
                showChangeDialog = false
            }
        ) {
            ReservationChangeDialog(
                onDismiss = {
                    showChangeDialog = false
                }
            )
        }
    }
}

@Composable
private fun ReservationDetailContent(
    reservation: Reservation,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MutedRoseBgLight
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 20.dp
                    ),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = "시술예정",
                    style =
                        MaterialTheme
                            .typography
                            .labelMedium,
                    color = SurfaceWhite,
                    fontWeight =
                        FontWeight.Bold,
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                5.dp
                            )
                        )
                        .background(
                            MutedRosePrimary
                        )
                        .padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                )

                Text(
                    text = reservation.dateTime,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    color = MutedRosePrimary,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        item {
            Column(
                modifier =
                    Modifier.padding(20.dp)
            ) {
                Text(
                    text =
                        reservation.shopName,
                    style =
                        MaterialTheme
                            .typography
                            .headlineSmall,
                    fontWeight =
                        FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector =
                            Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint =
                            MutedRosePrimary
                    )

                    Text(
                        text =
                            reservation.address,
                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium,
                        modifier =
                            Modifier.padding(
                                start = 10.dp
                            )
                    )
                }

                if (
                    reservation.design
                        .isNotBlank()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .clip(
                                RoundedCornerShape(
                                    6.dp
                                )
                            )
                            .background(
                                MaterialTheme
                                    .colorScheme
                                    .surfaceVariant
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "디자인",
                            style =
                                MaterialTheme
                                    .typography
                                    .labelMedium,
                            fontWeight =
                                FontWeight.Bold,
                            color =
                                TextSecondary
                        )

                        Text(
                            text =
                                reservation.design,
                            style =
                                MaterialTheme
                                    .typography
                                    .bodyMedium,
                            modifier =
                                Modifier.padding(
                                    top = 12.dp
                                )
                        )
                    }
                }
            }
        }

        item {
            HorizontalDivider(
                color = DividerGray,
                modifier =
                    Modifier.padding(
                        horizontal = 16.dp
                    )
            )
        }

        item {
            Column(
                modifier =
                    Modifier.padding(20.dp)
            ) {
                Text(
                    text = "결제 금액",
                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,
                    fontWeight =
                        FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Text(
                        text = "총 결제 금액",
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "%,d원".format(
                                reservation.price
                            ),
                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall,
                        color =
                            MutedRosePrimary,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun ReservationDetailResponse
        .toReservation(): Reservation {

    val reservationStatus =
        when (
            status.uppercase(
                Locale.US
            )
        ) {
            "COMPLETED" ->
                ReservationStatus.COMPLETED

            "CANCELLED",
            "CANCELED" ->
                ReservationStatus.CANCELLED

            else ->
                ReservationStatus.CONFIRMED
        }

    return Reservation(
        id = reservationId.toString(),
        status = reservationStatus,
        dateTime =
            formatReservedAt(
                reservedAt
            ),
        shopName = shopName,
        design =
            designName
                ?: "디자인 정보 없음",
        option = "",
        price = totalPrice,
        imageRes =
            R.drawable.img_nail_1,
        address = address,
        basePrice = totalPrice
    )
}

private fun formatReservedAt(
    reservedAt: String
): String {
    if (reservedAt.isBlank()) {
        return ""
    }

    return runCatching {
        val inputFormatter =
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.US
            ).apply {
                timeZone =
                    TimeZone.getTimeZone(
                        "UTC"
                    )
            }

        val date =
            inputFormatter.parse(
                reservedAt
            ) ?: return reservedAt

        val outputFormatter =
            SimpleDateFormat(
                "MM.dd (E) · a hh:mm",
                Locale.KOREAN
            )

        outputFormatter.format(date)
    }.getOrDefault(
        reservedAt
    )
}