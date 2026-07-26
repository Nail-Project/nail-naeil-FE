package com.example.nailnail.ui.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.example.nailnail.ui.main.home.components.EstimateUploadCard
import com.example.nailnail.ui.main.home.components.InProgressEstimateSection
import com.example.nailnail.ui.main.home.components.MagazineSection
import com.example.nailnail.ui.main.address.AddressMockState

@Composable
fun MainHomeScreen(
    onAddressClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onNeedUpgrade: () -> Unit,
    onStartEstimate: () -> Unit,
    onMagazineClick: (String) -> Unit,
    onEstimateClick: (EstimateSummary) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("전체") }
    var likedIds by remember { mutableStateOf(setOf<String>()) }

    val uploadSubtitle = when {
        HomeMockState.estimates.isEmpty() -> "최대 3장까지 가능해요"
        HomeMockState.needsUpgrade() -> "견적 진행은 완료될 때까지 최대 1개만 가능해요"
        else -> "더 많은 견적 받기"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onAddressClick)
                ) {
                    Text(
                        text = AddressMockState.currentAddressLabel(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = null)
                }
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "알림",
                    modifier = Modifier.clickable(onClick = onNotificationClick)
                )
            }

            Text(
                text = "받고 싶은 네일,\n사진으로 견적 받아보세요",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 20.dp, bottom = 16.dp)
            )

            EstimateUploadCard(
                subtitle = uploadSubtitle,
                activeEstimateCount = HomeMockState.estimates.size,
                onUploadClick = {
                    if (HomeMockState.needsUpgrade()) onNeedUpgrade() else onStartEstimate()
                },
                onAvatarClick = onNeedUpgrade
            )

            InProgressEstimateSection(
                estimates = HomeMockState.estimates,
                onSeeAllClick = {},
                onEstimateClick = onEstimateClick,
                modifier = Modifier.padding(top = 28.dp)
            )

            MagazineSection(
                items = MockMagazineItems,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                likedIds = likedIds,
                onLikeToggle = { id ->
                    likedIds = if (likedIds.contains(id)) likedIds - id else likedIds + id
                },
                onItemClick = { onMagazineClick(it.id) },
                modifier = Modifier.padding(top = 28.dp, bottom = 24.dp)
            )
        }
    }
}
