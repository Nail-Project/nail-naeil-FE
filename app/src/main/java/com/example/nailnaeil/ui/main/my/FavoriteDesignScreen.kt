package com.example.nailnaeil.ui.main.my

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.BookmarkedDesignItem
import com.example.nailnaeil.ui.theme.SurfaceWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteDesignScreen(
    onBackClick: () -> Unit,
    onDesignClick: (Long) -> Unit = {},
    viewModel: FavoriteDesignViewModel = viewModel { FavoriteDesignViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("찜한 디자인") },
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
                uiState.designs.isEmpty() -> Text(
                    text = "찜한 디자인이 없어요.",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(uiState.designs) { design ->
                        FavoriteDesignCard(
                            design = design,
                            onClick = { onDesignClick(design.designId) },
                            onHeartClick = { viewModel.removeWish(design.designId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteDesignCard(design: BookmarkedDesignItem, onClick: () -> Unit, onHeartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.83f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = design.imageUrl,
            contentDescription = design.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0x33000000), Color(0xB3000000))
                    )
                )
                .padding(top = 60.dp)
        ) {
            Text(
                text = design.title,
                style = MaterialTheme.typography.titleMedium,
                color = SurfaceWhite,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 16.dp)
            )
        }

        IconButton(
            onClick = onHeartClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(40.dp)
        ) {
            Icon(Icons.Filled.Favorite, contentDescription = "찜 해제", tint = SurfaceWhite)
        }
    }
}
