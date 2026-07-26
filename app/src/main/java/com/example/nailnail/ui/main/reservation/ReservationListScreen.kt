package com.example.nailnail.ui.main.reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nailnail.ui.main.reservation.components.ReservationCard
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.TextDisabled
import com.example.nailnail.ui.theme.TextSecondary

private val ReservationTabs = listOf("확정된 예약", "지난 예약")

@Composable
fun ReservationListScreen(onReservationClick: (String) -> Unit, onStartEstimate: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    var confirmedSortAscending by remember { mutableStateOf(true) }
    var pastSortAscending by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "예약",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            ReservationTabs.forEachIndexed { index, title ->
                val selected = index == selectedTab
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = index },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) MaterialTheme.colorScheme.onBackground else TextDisabled,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(if (selected) MaterialTheme.colorScheme.onBackground else androidx.compose.ui.graphics.Color.Transparent)
                    )
                }
            }
        }

        if (selectedTab == 0) {
            ConfirmedReservationTab(
                sortAscending = confirmedSortAscending,
                onToggleSort = { confirmedSortAscending = !confirmedSortAscending },
                onReservationClick = onReservationClick,
                onStartEstimate = onStartEstimate
            )
        } else {
            PastReservationTab(
                sortAscending = pastSortAscending,
                onToggleSort = { pastSortAscending = !pastSortAscending },
                onReservationClick = onReservationClick
            )
        }
    }
}

@Composable
private fun ConfirmedReservationTab(
    sortAscending: Boolean,
    onToggleSort: () -> Unit,
    onReservationClick: (String) -> Unit,
    onStartEstimate: () -> Unit
) {
    val confirmed = ReservationMockState.confirmed

    if (confirmed.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "확정된 예약이 없어요.\n지금 바로 견적을 요청해보세요!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onStartEstimate,
                colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text("디자인 견적받기")
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 28.dp)
    ) {
        item {
            ReservationSectionHeader(
                countLabel = "총 ${confirmed.size}건",
                sortAscending = sortAscending,
                onToggleSort = onToggleSort
            )
        }

        items(confirmed, key = { it.id }) { reservation ->
            ReservationCard(
                reservation = reservation,
                onDetailClick = { onReservationClick(reservation.id) },
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(20.dp)
            ) {
                Text(
                    text = "ⓘ  방문 전 확인해주세요",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
                Text(
                    text = "기존 젤 제거가 포함된 시술입니다. 예약 시간보다 10분 일찍 방문해 주시면 더욱 원활한 시술이 가능합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 26.dp, top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun PastReservationTab(
    sortAscending: Boolean,
    onToggleSort: () -> Unit,
    onReservationClick: (String) -> Unit
) {
    val past = ReservationMockState.past

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 28.dp)
    ) {
        item {
            ReservationSectionHeader(
                countLabel = "총 ${past.size}건",
                sortAscending = sortAscending,
                onToggleSort = onToggleSort
            )
        }

        items(past, key = { it.id }) { reservation ->
            ReservationCard(
                reservation = reservation,
                onDetailClick = { onReservationClick(reservation.id) },
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun ReservationSectionHeader(countLabel: String, sortAscending: Boolean, onToggleSort: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = countLabel, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            text = if (sortAscending) "방문일자 ↑" else "방문일자 ↓",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable(onClick = onToggleSort)
        )
    }
}
