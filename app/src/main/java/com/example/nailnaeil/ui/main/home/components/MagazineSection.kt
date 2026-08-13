package com.example.nailnaeil.ui.main.home.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.DesignSummary
import com.example.nailnaeil.ui.main.home.MagazineCategories
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun MagazineSection(
    items: List<DesignSummary>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onLikeToggle: (DesignSummary) -> Unit,
    onItemClick: (DesignSummary) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "디자인 매거진",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        LazyRow(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(MagazineCategories) { category ->
                val isSelected = category == selectedCategory
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) TextMain else TextSecondary,
                    modifier = Modifier.clickable { onCategorySelected(category) }
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            items.chunked(2).forEachIndexed { index, rowItems ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { item ->
                        MagazineCard(
                            item = item,
                            onLikeToggle = { onLikeToggle(item) },
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
    item: DesignSummary,
    onLikeToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(0.83f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.83f)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0x33000000), Color(0xCC000000))
                    )
                )
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SurfaceWhite,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = null,
                        tint = SurfaceWhite,
                        modifier = Modifier.height(14.dp)
                    )
                    Text(
                        text = " ${item.viewCount}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SurfaceWhite,
                        fontSize = 11.sp
                    )
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = SurfaceWhite,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .height(14.dp)
                    )
                    Text(
                        text = " ${item.wishCount}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SurfaceWhite,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Icon(
            imageVector = if (item.isBookmarked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "좋아요",
            tint = SurfaceWhite,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clickable(onClick = onLikeToggle)
        )
    }
}
