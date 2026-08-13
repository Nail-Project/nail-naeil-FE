package com.example.nailnaeil.ui.main.design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.DesignSummary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun SimilarDesignsScreen(
    designId: Long,
    onBackClick: () -> Unit,
    onDesignClick: (Long) -> Unit,
    viewModel: SimilarDesignsViewModel = viewModel { SimilarDesignsViewModel(designId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceWhite)) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(20.dp)
                    .clickable(onClick = onBackClick)
            )
            Text(
                text = "비슷한 디자인",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        when {
            uiState.isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            uiState.designs.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.errorMessage ?: "표시할 디자인이 없어요.",
                    color = TextSecondary
                )
            }
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.designs) { design ->
                    SimilarDesignGridCard(
                        design = design,
                        onClick = { onDesignClick(design.designId) },
                        onLikeToggle = { viewModel.toggleWish(design) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SimilarDesignGridCard(
    design: DesignSummary,
    onClick: () -> Unit,
    onLikeToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(12.dp))
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
                .padding(12.dp)
        ) {
            Text(
                text = design.title,
                style = MaterialTheme.typography.bodyMedium,
                color = SurfaceWhite,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
        Icon(
            imageVector = if (design.isBookmarked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "찜하기",
            tint = SurfaceWhite,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clickable(onClick = onLikeToggle)
        )
    }
}
