package com.example.nailnaeil.ui.main.reservation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
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
import kotlinx.coroutines.launch

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
    var reservation by remember(reservationId) {
        mutableStateOf<ReservationDetailResponse?>(null)
    }

    var isLoading by remember(reservationId) {
        mutableStateOf(true)
    }

    var errorMessage by remember(reservationId) {
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

    var changePhoneNumber by remember {
        mutableStateOf<String?>(null)
    }

    var isCancelling by remember {
        mutableStateOf(false)
    }

    var isLoadingChangeInfo by remember {
        mutableStateOf(false)
    }

    val context =
        LocalContext.current

    val coroutineScope =
        rememberCoroutineScope()

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
                reservation = response
            }
            .onFailure { exception ->
                reservation = null
                errorMessage =
                    exception.message
                        ?: "예약 정보를 불러오지 못했습니다."
            }

        isLoading = false
    }

    val currentReservation =
        reservation

    Scaffold(
        containerColor =
            SurfaceWhite,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (
                            currentReservation
                                ?.status
                                ?.uppercase()
                        ) {
                            "COMPLETED" ->
                                "지난 예약"

                            "CANCELLED",
                            "CANCELED" ->
                                "취소된 예약"

                            else ->
                                "확정된 예약"
                        }
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick =
                            onBackClick
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored
                                    .Filled
                                    .ArrowBack,

                            contentDescription =
                                "뒤로가기"
                        )
                    }
                }
            )
        },

        bottomBar = {
            if (
                currentReservation != null &&
                currentReservation.status.equals(
                    "CONFIRMED",
                    ignoreCase = true
                )
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(
                                SurfaceWhite
                            )
                            .padding(
                                16.dp
                            ),

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            8.dp
                        )
                ) {

                    Button(
                        onClick = {
                            if (!isCancelling) {
                                activeSheet =
                                    ReservationSheet
                                        .CANCEL_CONFIRM
                            }
                        },

                        enabled =
                            !isCancelling &&
                                    !isLoadingChangeInfo,

                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        DangerBgLight,

                                    contentColor =
                                        DangerRed
                                ),

                        shape =
                            RoundedCornerShape(
                                8.dp
                            ),

                        modifier =
                            Modifier
                                .weight(1f)
                                .height(56.dp)
                    ) {

                        Text(
                            text =
                                if (isCancelling) {
                                    "취소 중..."
                                } else {
                                    "예약 취소"
                                },

                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = {

                            if (isLoadingChangeInfo) {
                                return@OutlinedButton
                            }

                            isLoadingChangeInfo =
                                true

                            coroutineScope.launch {

                                reservationRepository
                                    .getReservationChangeInfo(
                                        reservationId =
                                            currentReservation
                                                .reservationId
                                    )
                                    .onSuccess { response ->

                                        isLoadingChangeInfo =
                                            false

                                        changePhoneNumber =
                                            response
                                                .shopPhoneNumber

                                        if (
                                            response
                                                .shopPhoneNumber
                                                .isBlank()
                                        ) {

                                            Toast.makeText(
                                                context,
                                                "샵 연락처가 없습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        } else {

                                            showChangeDialog =
                                                true
                                        }
                                    }
                                    .onFailure { exception ->

                                        isLoadingChangeInfo =
                                            false

                                        Toast.makeText(
                                            context,

                                            exception.message
                                                ?: "예약 변경 정보를 불러오지 못했습니다.",

                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        },

                        enabled =
                            !isCancelling &&
                                    !isLoadingChangeInfo,

                        shape =
                            RoundedCornerShape(
                                8.dp
                            ),

                        modifier =
                            Modifier
                                .weight(1f)
                                .height(56.dp)
                    ) {

                        Text(
                            text =
                                if (
                                    isLoadingChangeInfo
                                ) {
                                    "불러오는 중..."
                                } else {
                                    "예약 변경"
                                },

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
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                padding
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color =
                            MutedRosePrimary
                    )
                }
            }

            errorMessage != null -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                padding
                            )
                            .padding(
                                24.dp
                            ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Text(
                        text =
                            errorMessage
                                ?: "오류가 발생했습니다.",

                        color =
                            TextSecondary
                    )

                    Button(
                        onClick = {
                            retryCount += 1
                        },

                        modifier =
                            Modifier.padding(
                                top = 20.dp
                            )
                    ) {
                        Text(
                            "다시 시도"
                        )
                    }
                }
            }

            currentReservation != null -> {
                ReservationDetailContent(
                    reservation =
                        currentReservation,

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                padding
                            )
                )
            }
        }
    }

    if (
        activeSheet !=
        ReservationSheet.NONE &&
        currentReservation != null
    ) {
        val sheetState =
            rememberModalBottomSheetState()

        val reservationForSheet =
            currentReservation
                .toReservation()

        ModalBottomSheet(
            onDismissRequest = {
                if (!isCancelling) {
                    activeSheet =
                        ReservationSheet.NONE
                }
            },

            sheetState =
                sheetState,

            containerColor =
                SurfaceWhite
        ) {

            when (
                activeSheet
            ) {

                ReservationSheet
                    .CANCEL_CONFIRM -> {

                    ReservationCancelConfirmSheet(
                        reservation =
                            reservationForSheet,

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
                            if (!isCancelling) {
                                activeSheet =
                                    ReservationSheet.NONE
                            }
                        },

                        onComplete = { reason ->

                            if (!isCancelling) {

                                isCancelling =
                                    true

                                activeSheet =
                                    ReservationSheet.NONE

                                coroutineScope.launch {

                                    reservationRepository
                                        .cancelReservation(
                                            reservationId =
                                                currentReservation
                                                    .reservationId,

                                            reason =
                                                reason
                                        )
                                        .onSuccess {

                                            isCancelling =
                                                false

                                            Toast.makeText(
                                                context,
                                                "예약이 취소되었습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            onCancelled()
                                        }
                                        .onFailure { exception ->

                                            isCancelling =
                                                false

                                            Toast.makeText(
                                                context,

                                                exception.message
                                                    ?: "예약 취소에 실패했습니다.",

                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            }
                        }
                    )
                }

                ReservationSheet.NONE ->
                    Unit
            }
        }
    }

    if (
        showChangeDialog
    ) {
        Dialog(
            onDismissRequest = {
                showChangeDialog =
                    false
            }
        ) {

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                16.dp
                            )
                        )
                        .background(
                            SurfaceWhite
                        )
                        .padding(
                            24.dp
                        )
            ) {

                Text(
                    text =
                        "예약 변경",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "예약 일정 변경은 샵에 직접 문의해주세요.",

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,

                    color =
                        TextSecondary,

                    modifier =
                        Modifier.padding(
                            top = 12.dp
                        )
                )

                Text(
                    text =
                        changePhoneNumber
                            .orEmpty(),

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.Bold,

                    modifier =
                        Modifier.padding(
                            top = 20.dp
                        )
                )

                Button(
                    onClick = {

                        val phoneNumber =
                            changePhoneNumber

                        if (
                            !phoneNumber
                                .isNullOrBlank()
                        ) {

                            val clipboardManager =
                                context
                                    .getSystemService(
                                        Context.CLIPBOARD_SERVICE
                                    )
                                        as ClipboardManager

                            val clip =
                                ClipData.newPlainText(
                                    "샵 연락처",
                                    phoneNumber
                                )

                            clipboardManager
                                .setPrimaryClip(
                                    clip
                                )

                            Toast.makeText(
                                context,
                                "샵 연락처를 복사했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                            showChangeDialog =
                                false
                        }
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 24.dp
                            )
                ) {

                    Text(
                        "연락처 복사"
                    )
                }

                OutlinedButton(
                    onClick = {
                        showChangeDialog =
                            false
                    },

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 8.dp
                            )
                ) {

                    Text(
                        "닫기"
                    )
                }
            }
        }
    }
}

@Composable
private fun ReservationDetailContent(
    reservation: ReservationDetailResponse,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier =
            modifier
    ) {

        item {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            MutedRoseBgLight
                        )
                        .padding(
                            horizontal =
                                16.dp,

                            vertical =
                                20.dp
                        ),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        reservationStatusLabel(
                            reservation.status
                        ),

                    color =
                        SurfaceWhite,

                    fontWeight =
                        FontWeight.Bold,

                    modifier =
                        Modifier
                            .clip(
                                RoundedCornerShape(
                                    5.dp
                                )
                            )
                            .background(
                                MutedRosePrimary
                            )
                            .padding(
                                horizontal =
                                    10.dp,

                                vertical =
                                    6.dp
                            )
                )

                Text(
                    text =
                        formatReservedAt(
                            reservation
                                .reservedAt
                        ),

                    color =
                        MutedRosePrimary,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        if (
            reservation.images
                .isNotEmpty()
        ) {
            item {
                AsyncImage(
                    model =
                        reservation.images
                            .first(),

                    contentDescription =
                        reservation.designName
                            ?: "네일 디자인",

                    contentScale =
                        ContentScale.Crop,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(
                                220.dp
                            )
                )
            }
        }

        item {
            Column(
                modifier =
                    Modifier.padding(
                        20.dp
                    )
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
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 20.dp
                            ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled
                                .LocationOn,

                        contentDescription =
                            null,

                        tint =
                            MutedRosePrimary
                    )

                    Text(
                        text =
                            buildAddress(
                                reservation
                            ),

                        modifier =
                            Modifier.padding(
                                start = 10.dp
                            )
                    )
                }

                if (
                    !reservation
                        .shopPhoneNumber
                        .isNullOrBlank()
                ) {
                    DetailTextRow(
                        title =
                            "샵 연락처",

                        value =
                            reservation
                                .shopPhoneNumber
                                .orEmpty()
                    )
                }

                DetailTextRow(
                    title =
                        "시술 부위",

                    value =
                        nailTypeLabel(
                            reservation.nailType
                        )
                )

                if (
                    !reservation
                        .designName
                        .isNullOrBlank()
                ) {

                    DetailTextRow(
                        title =
                            "디자인",

                        value =
                            reservation
                                .designName
                                .orEmpty()
                    )
                }

                if (
                    !reservation
                        .shopComment
                        .isNullOrBlank()
                ) {

                    DetailTextRow(
                        title =
                            "샵 코멘트",

                        value =
                            reservation
                                .shopComment
                                .orEmpty()
                    )
                }
            }
        }

        item {

            HorizontalDivider(
                color =
                    DividerGray,

                modifier =
                    Modifier.padding(
                        horizontal =
                            16.dp
                    )
            )
        }

        item {

            Column(
                modifier =
                    Modifier.padding(
                        20.dp
                    )
            ) {

                Text(
                    text =
                        "결제 금액",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    fontWeight =
                        FontWeight.Bold
                )

                PriceRow(
                    label =
                        "기본 시술 금액",

                    price =
                        reservation.basePrice
                )

                PriceRow(
                    label =
                        "제거 비용",

                    price =
                        reservation.removalPrice
                )

                PriceRow(
                    label =
                        "추가 비용",

                    price =
                        reservation.extraPrice
                )

                HorizontalDivider(
                    modifier =
                        Modifier.padding(
                            top = 20.dp
                        ),

                    color =
                        DividerGray
                )

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 20.dp
                            ),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text =
                            "총 결제 금액",

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "%,d원".format(
                                reservation
                                    .totalPrice
                            ),

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

@Composable
private fun DetailTextRow(
    title: String,
    value: String
) {
    if (
        value.isBlank()
    ) {
        return
    }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp
                ),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text =
                title,

            color =
                TextSecondary
        )

        Text(
            text =
                value,

            fontWeight =
                FontWeight.Bold
        )
    }
}

@Composable
private fun PriceRow(
    label: String,
    price: Int
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp
                ),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text =
                label,

            color =
                TextSecondary
        )

        Text(
            text =
                "%,d원".format(
                    price
                ),

            fontWeight =
                FontWeight.Bold
        )
    }
}

private fun ReservationDetailResponse
        .toReservation(): Reservation {

    return Reservation(
        id =
            reservationId
                .toString(),

        status =
            when (
                status.uppercase(
                    Locale.US
                )
            ) {

                "COMPLETED" ->
                    ReservationStatus
                        .COMPLETED

                "CANCELLED",
                "CANCELED" ->
                    ReservationStatus
                        .CANCELLED

                else ->
                    ReservationStatus
                        .CONFIRMED
            },

        dateTime =
            formatReservedAt(
                reservedAt
            ),

        shopName =
            shopName,

        design =
            designName
                ?: "디자인 정보 없음",

        option =
            "",

        price =
            totalPrice,

        imageUrl =
            images.firstOrNull(),

        address =
            address,

        comment =
            shopComment
                .orEmpty(),

        basePrice =
            basePrice,

        optionPrice =
            removalPrice +
                    extraPrice
    )
}

private fun buildAddress(
    reservation:
    ReservationDetailResponse
): String {

    return listOfNotNull(
        reservation.address
            .takeIf {
                it.isNotBlank()
            },

        reservation.addressDetail
            ?.takeIf {
                it.isNotBlank()
            }
    ).joinToString(
        " "
    )
}

private fun reservationStatusLabel(
    status: String
): String {

    return when (
        status.uppercase()
    ) {

        "COMPLETED" ->
            "시술완료"

        "CANCELLED",
        "CANCELED" ->
            "예약취소"

        else ->
            "시술예정"
    }
}

private fun nailTypeLabel(
    nailType: String
): String {

    return when (
        nailType.uppercase()
    ) {

        "HAND" ->
            "손 네일"

        "PEDICURE" ->
            "페디큐어"

        "BOTH" ->
            "손·발 네일"

        else ->
            nailType
    }
}

private fun formatReservedAt(
    reservedAt: String
): String {

    if (
        reservedAt.isBlank()
    ) {
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
            )
                ?: return reservedAt

        val outputFormatter =
            SimpleDateFormat(
                "MM.dd (E) · a hh:mm",
                Locale.KOREAN
            )

        outputFormatter.format(
            date
        )

    }.getOrDefault(
        reservedAt
    )
}