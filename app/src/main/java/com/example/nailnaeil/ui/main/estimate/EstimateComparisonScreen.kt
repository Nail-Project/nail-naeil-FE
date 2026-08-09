package com.example.nailnaeil.ui.main.estimate

import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.nailnaeil.data.repository.EstimateResultRepository
import com.example.nailnaeil.data.repository.EstimateTimeRepository
import com.example.nailnaeil.data.repository.ReservationRepository
import com.example.nailnaeil.ui.common.PhotoPlaceholder
import com.example.nailnaeil.ui.main.estimate.components.ShopEstimateCard
import com.example.nailnaeil.ui.main.reservation.Reservation
import com.example.nailnaeil.ui.main.reservation.ReservationStatus
import com.example.nailnaeil.ui.main.reservation.components.ReservationConfirmDialog
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary
import kotlinx.coroutines.launch

private val DateFilters = listOf(
    "전체",
    "7/4 (토)",
    "7/5 (일)"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimateComparisonScreen(
    estimateId: String,
    estimateResultRepository: EstimateResultRepository,
    estimateTimeRepository: EstimateTimeRepository,
    reservationRepository: ReservationRepository,
    onBackClick: () -> Unit,
    onShopDetailClick: (String) -> Unit,
    onReservationConfirmed: (Reservation) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val estimate = remember(estimateId) {
        MockEstimateItems.find {
            it.id == estimateId
        }
    }

    var selectedDateFilter by remember {
        mutableIntStateOf(0)
    }

    var shopEstimates by remember {
        mutableStateOf<List<ShopEstimate>>(
            emptyList()
        )
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var reservingShop by remember {
        mutableStateOf<ShopEstimate?>(null)
    }

    var reservingSlot by remember {
        mutableStateOf<ReservationTimeSlot?>(null)
    }

    var isReserving by remember {
        mutableStateOf(false)
    }

    fun loadEstimateResult() {
        val requestId =
            estimateId.toLongOrNull()

        if (requestId == null) {
            errorMessage =
                "올바르지 않은 견적 번호입니다."
            isLoading = false
            return
        }

        isLoading = true
        errorMessage = null

        coroutineScope.launch {

            estimateResultRepository
                .getEstimateResult(
                    requestId = requestId
                )
                .onSuccess { result ->

                    val loadedShops =
                        mutableListOf<ShopEstimate>()

                    for (
                    proposal in result.responses
                    ) {
                        val timeResult =
                            estimateTimeRepository
                                .getProposalTimes(
                                    proposalId =
                                        proposal.id
                                )

                        val slots =
                            timeResult
                                .getOrNull()
                                ?.proposalTimes
                                ?.map { proposalTime ->

                                    ReservationTimeSlot(
                                        timeId =
                                            proposalTime.id,
                                        displayText =
                                            formatProposalDateTime(
                                                proposalTime
                                                    .proposalDateTime
                                            )
                                    )
                                }
                                ?: proposal
                                    .proposalDateTimes
                                    .map { dateTime ->

                                        ReservationTimeSlot(
                                            timeId = null,
                                            displayText =
                                                formatProposalDateTime(
                                                    dateTime
                                                )
                                        )
                                    }

                        loadedShops.add(
                            ShopEstimate(
                                id =
                                    proposal.id
                                        .toString(),

                                proposalId =
                                    proposal.id,

                                shopName =
                                    proposal.shop.name,

                                isWaiting =
                                    false,

                                removalIncluded =
                                    false,

                                rating =
                                    null,

                                distance =
                                    proposal.shop.address,

                                priceText =
                                    "%,d원".format(
                                        proposal.totalPrice
                                    ),

                                isLowestPrice =
                                    false,

                                comment =
                                    null,

                                timeSlots =
                                    slots
                            )
                        )
                    }

                    val minimumPrice =
                        result.responses
                            .minOfOrNull {
                                it.totalPrice
                            }

                    shopEstimates =
                        loadedShops.mapIndexed {
                                index,
                                shop ->

                            val price =
                                result.responses
                                    .getOrNull(index)
                                    ?.totalPrice

                            shop.copy(
                                isLowestPrice =
                                    minimumPrice != null &&
                                            price ==
                                            minimumPrice
                            )
                        }

                    isLoading = false
                }
                .onFailure { exception ->

                    errorMessage =
                        exception.message
                            ?: "견적 결과를 불러오지 못했습니다."

                    isLoading = false
                }
        }
    }

    LaunchedEffect(estimateId) {
        loadEstimateResult()
    }

    Scaffold(
        containerColor =
            SurfaceWhite,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "견적 비교 결과"
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
        }
    ) { padding ->

        when {

            isLoading -> {

                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(padding),

                    contentAlignment =
                        Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {

                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(24.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Text(
                        text =
                            errorMessage.orEmpty()
                    )

                    Button(
                        onClick = {
                            loadEstimateResult()
                        },

                        modifier =
                            Modifier.padding(
                                top = 16.dp
                            )
                    ) {
                        Text(
                            "다시 시도"
                        )
                    }
                }
            }

            else -> {

                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(padding),

                    contentPadding =
                        PaddingValues(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            12.dp
                        )
                ) {

                    item {

                        if (estimate != null) {

                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .clip(
                                            RoundedCornerShape(
                                                20.dp
                                            )
                                        )
                                        .background(
                                            SurfaceWhite
                                        )
                                        .border(
                                            1.dp,
                                            DividerGray,
                                            RoundedCornerShape(
                                                20.dp
                                            )
                                        )
                                        .padding(
                                            20.dp
                                        )
                            ) {

                                PhotoPlaceholder(
                                    modifier =
                                        Modifier.size(
                                            64.dp
                                        )
                                )

                                Column(
                                    modifier =
                                        Modifier.padding(
                                            start = 14.dp
                                        )
                                ) {

                                    Text(
                                        text =
                                            estimate.styleKeywords,

                                        style =
                                            MaterialTheme
                                                .typography
                                                .bodyLarge,

                                        fontWeight =
                                            FontWeight.Bold
                                    )

                                    Row(
                                        modifier =
                                            Modifier.padding(
                                                top = 6.dp
                                            )
                                    ) {

                                        Text(
                                            text =
                                                estimate.status,

                                            style =
                                                MaterialTheme
                                                    .typography
                                                    .labelMedium,

                                            color =
                                                MutedRosePrimary,

                                            fontWeight =
                                                FontWeight.Bold
                                        )

                                        Text(
                                            text =
                                                " · ${estimate.replyStatus}",

                                            style =
                                                MaterialTheme
                                                    .typography
                                                    .labelMedium,

                                            color =
                                                TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {

                        Row(
                            modifier =
                                Modifier.horizontalScroll(
                                    rememberScrollState()
                                ),

                            horizontalArrangement =
                                Arrangement.spacedBy(
                                    8.dp
                                )
                        ) {

                            DateFilters
                                .forEachIndexed {
                                        index,
                                        label ->

                                    val selected =
                                        index ==
                                                selectedDateFilter

                                    Text(
                                        text =
                                            label,

                                        style =
                                            MaterialTheme
                                                .typography
                                                .labelMedium,

                                        color =
                                            if (
                                                selected
                                            ) {
                                                MutedRosePrimary
                                            } else {
                                                TextSecondary
                                            },

                                        modifier =
                                            Modifier
                                                .clip(
                                                    RoundedCornerShape(
                                                        6.dp
                                                    )
                                                )
                                                .background(
                                                    if (
                                                        selected
                                                    ) {
                                                        MutedRoseBgLight
                                                    } else {
                                                        SurfaceWhite
                                                    }
                                                )
                                                .border(
                                                    1.dp,
                                                    if (
                                                        selected
                                                    ) {
                                                        MutedRosePrimary
                                                    } else {
                                                        DividerGray
                                                    },
                                                    RoundedCornerShape(
                                                        6.dp
                                                    )
                                                )
                                                .clickable {
                                                    selectedDateFilter =
                                                        index
                                                }
                                                .padding(
                                                    horizontal =
                                                        12.dp,
                                                    vertical =
                                                        8.dp
                                                )
                                    )
                                }
                        }
                    }

                    item {

                        Text(
                            text =
                                "최저가 ∨",

                            style =
                                MaterialTheme
                                    .typography
                                    .bodyMedium,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    if (
                        shopEstimates.isEmpty()
                    ) {

                        item {

                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical =
                                                60.dp
                                        ),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Text(
                                    text =
                                        "도착한 견적이 없습니다.",

                                    color =
                                        TextSecondary
                                )
                            }
                        }
                    }

                    items(
                        items =
                            shopEstimates,

                        key = {
                            it.id
                        }
                    ) { shop ->

                        ShopEstimateCard(
                            shop =
                                shop,

                            onDetailClick = {
                                onShopDetailClick(
                                    shop.id
                                )
                            },

                            onReserveClick = { slot ->

                                reservingShop =
                                    shop

                                reservingSlot =
                                    slot
                            }
                        )
                    }
                }
            }
        }
    }

    val shop = reservingShop
    val slot = reservingSlot

    if (
        shop != null &&
        slot != null
    ) {

        Dialog(
            onDismissRequest = {

                if (!isReserving) {
                    reservingShop =
                        null

                    reservingSlot =
                        null
                }
            }
        ) {

            ReservationConfirmDialog(
                shopName =
                    shop.shopName,

                dateTime =
                    slot.displayText
                        .replace(
                            "\n",
                            " · "
                        ),

                priceText =
                    shop.priceText
                        .orEmpty(),

                onDismiss = {

                    if (!isReserving) {
                        reservingShop =
                            null

                        reservingSlot =
                            null
                    }
                },

                onConfirm = {

                    if (isReserving) {
                        return@ReservationConfirmDialog
                    }

                    val proposalId =
                        shop.proposalId

                    val timeId =
                        slot.timeId

                    if (
                        proposalId == null
                    ) {

                        Toast.makeText(
                            context,
                            "견적 응답 정보가 없습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@ReservationConfirmDialog
                    }

                    if (
                        timeId == null
                    ) {

                        Toast.makeText(
                            context,
                            "예약 가능한 시간 정보를 불러오지 못했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@ReservationConfirmDialog
                    }

                    isReserving = true

                    coroutineScope.launch {

                        reservationRepository
                            .createReservation(
                                proposalId =
                                    proposalId,

                                timeId =
                                    timeId
                            )
                            .onSuccess {
                                    response ->

                                isReserving =
                                    false

                                reservingShop =
                                    null

                                reservingSlot =
                                    null

                                onReservationConfirmed(
                                    Reservation(
                                        id =
                                            response
                                                .reservationId
                                                .toString(),

                                        status =
                                            when (
                                                response.status
                                                    .uppercase()
                                            ) {

                                                "CANCELLED" ->
                                                    ReservationStatus
                                                        .CANCELLED

                                                "COMPLETED" ->
                                                    ReservationStatus
                                                        .COMPLETED

                                                else ->
                                                    ReservationStatus
                                                        .CONFIRMED
                                            },

                                        dateTime =
                                            formatProposalDateTime(
                                                response.reservedAt
                                            ),

                                        shopName =
                                            response.shopName,

                                        design =
                                            estimate
                                                ?.styleKeywords
                                                .orEmpty(),

                                        option =
                                            "",

                                        price =
                                            response.totalPrice,

                                        proposalId =
                                            proposalId
                                    )
                                )
                            }
                            .onFailure {
                                    exception ->

                                isReserving =
                                    false

                                Toast.makeText(
                                    context,

                                    exception.message
                                        ?: "예약에 실패했습니다.",

                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
            )
        }
    }
}

private fun formatProposalDateTime(
    value: String
): String {

    if (
        value.length < 16
    ) {
        return value
    }

    return try {

        val month =
            value.substring(
                5,
                7
            )

        val day =
            value.substring(
                8,
                10
            )

        val hour =
            value.substring(
                11,
                13
            )

        val minute =
            value.substring(
                14,
                16
            )

        "$month.$day\n$hour:$minute"

    } catch (
        exception: Exception
    ) {
        value
    }
}