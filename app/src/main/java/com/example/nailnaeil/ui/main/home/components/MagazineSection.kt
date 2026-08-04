package com.example.nailnaeil.ui.main.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.ui.common.PhotoPlaceholder
import com.example.nailnaeil.ui.main.home.MagazineCategories
import com.example.nailnaeil.ui.main.home.MagazineItem
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun MagazineSection(
    items: List<MagazineItem>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    likedIds: Set<String>,
    onLikeToggle: (String) -> Unit,
    onItemClick: (MagazineItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = "디자인 매거진", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        LazyRow(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(MagazineCategories) { category ->
                FilterChip(
                    selected = category == selectedCategory,
                    onClick = { onCategorySelected(category) },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MutedRosePrimary,
                        selectedLabelColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        ) {
            items.chunked(2).forEachIndexed { index, rowItems ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { item ->
                        MagazineCard(
                            item = item,
                            isLiked = likedIds.contains(item.id),
                            onLikeToggle = { onLikeToggle(item.id) },
                            onClick = { onItemClick(item) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun MagazineCard(
    item: MagazineItem,
    isLiked: Boolean,
    onLikeToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        Box {
            PhotoPlaceholder(modifier = Modifier.aspectRatio(1f))
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "좋아요",
                tint = if (isLiked) MutedRosePrimary else TextSecondary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clickable(onClick = onLikeToggle)
            )
        }
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "◎ ${item.viewCount}  ♡ ${item.likeCount}",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}
