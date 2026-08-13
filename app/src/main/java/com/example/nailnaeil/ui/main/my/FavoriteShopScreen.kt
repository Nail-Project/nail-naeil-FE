package com.example.nailnaeil.ui.main.my

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.ShopSummary
import com.example.nailnaeil.ui.theme.DividerGray
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteShopScreen(
    onBackClick: () -> Unit,
    onShopClick: (Long) -> Unit = {},
    viewModel: FavoriteShopViewModel = viewModel { FavoriteShopViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("찜한 매장") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                uiState.errorMessage != null -> Text(
                    text = uiState.errorMessage ?: "",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
                uiState.shops.isEmpty() -> Text(
                    text = "찜한 매장이 없어요.",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.shops) { shop ->
                        FavoriteShopRow(shop = shop, onClick = { onShopClick(shop.shopId) })
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteShopRow(shop: ShopSummary, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = shop.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(text = "★", color = Color(0xFFFFAA00), style = MaterialTheme.typography.bodyLarge)
            Text(
                text = String.format("%.1f", shop.rating),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = "(${shop.reviewCount})",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(start = 4.dp)
            )
            val location = shop.adminDongName ?: shop.districtName
            if (location != null) {
                Text(text = "·", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, modifier = Modifier.padding(start = 6.dp))
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }

        AsyncImage(
            model = shop.thumbnailImageUrl,
            contentDescription = "${shop.name} 매장 사진",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(4.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(1.dp)
                .background(DividerGray)
        )
    }
}
