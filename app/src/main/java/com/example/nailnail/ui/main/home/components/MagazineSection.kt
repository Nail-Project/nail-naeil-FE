package com.example.nailnail.ui.main.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.nailnail.ui.common.PhotoPlaceholder
import com.example.nailnail.ui.main.home.MagazineCategories
import com.example.nailnail.ui.main.home.MagazineItem
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.TextSecondary

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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                MagazineCard(
                    item = item,
                    isLiked = likedIds.contains(item.id),
                    onLikeToggle = { onLikeToggle(item.id) },
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

@Composable
private fun MagazineCard(
    item: MagazineItem,
    isLiked: Boolean,
    onLikeToggle: () -> Unit,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
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
