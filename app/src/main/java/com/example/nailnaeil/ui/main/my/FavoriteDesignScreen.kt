package com.example.nailnaeil.ui.main.my

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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nailnaeil.ui.theme.SurfaceWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteDesignScreen(
    designs: List<FavoriteDesignItem>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    hasNext: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onLoadMore: () -> Unit,
    onDesignClick: (Long) -> Unit
) {
    val gridState = rememberLazyGridState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount

            if (totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItemIndex =
                    layoutInfo.visibleItemsInfo
                        .lastOrNull()
                        ?.index
                        ?: 0

                lastVisibleItemIndex >= totalItemsCount - 4
            }
        }
    }

    LaunchedEffect(
        shouldLoadMore,
        hasNext,
        isLoadingMore,
        isLoading
    ) {
        if (
            shouldLoadMore &&
            hasNext &&
            !isLoadingMore &&
            !isLoading
        ) {
            onLoadMore()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("찜한 디자인")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null && designs.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {
                    Text(
                        text = errorMessage
                    )

                    Button(
                        onClick = onRetryClick,
                        modifier =
                            Modifier.padding(top = 16.dp)
                    ) {
                        Text("다시 시도")
                    }
                }
            }

            designs.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("찜한 디자인이 없습니다.")
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding =
                        PaddingValues(10.dp),
                    horizontalArrangement =
                        Arrangement.spacedBy(6.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {
                    items(
                        items = designs,
                        key = {
                            it.designId
                        }
                    ) { design ->

                        FavoriteDesignCard(
                            design = design,
                            onClick = {
                                onDesignClick(
                                    design.designId
                                )
                            }
                        )
                    }

                    if (isLoadingMore) {
                        item(
                            key = "loading_more"
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                contentAlignment =
                                    Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier =
                                        Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteDesignCard(
    design: FavoriteDesignItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.83f)
            .clip(
                RoundedCornerShape(8.dp)
            )
            .clickable(
                onClick = onClick
            )
    ) {
        AsyncImage(
            model = design.imageUrl,
            contentDescription =
                design.designName,
            contentScale =
                ContentScale.Crop,
            modifier =
                Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(
                    Alignment.BottomCenter
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x33000000),
                            Color(0xB3000000)
                        )
                    )
                )
                .padding(
                    top = 60.dp
                )
        ) {
            Text(
                text =
                    design.designName,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                color =
                    SurfaceWhite,
                fontWeight =
                    FontWeight.Bold,
                maxLines = 1,
                modifier =
                    Modifier.padding(
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    )
            )
        }

        IconButton(
            onClick = {
                // TODO:
                // 찜 토글 API 명세 확정 후 연결
            },
            modifier = Modifier
                .align(
                    Alignment.TopEnd
                )
                .size(40.dp)
        ) {
            Icon(
                imageVector =
                    Icons.Filled.Favorite,
                contentDescription =
                    "찜",
                tint =
                    SurfaceWhite
            )
        }
    }
}