package com.example.nailnaeil.ui.quote.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nailnaeil.ui.quote.QuoteUiState
import com.example.nailnaeil.ui.quote.components.CloseTitleHeader
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.theme.AppBackground
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

private const val MAX_PHOTOS = 3

@Composable
fun PhotoUploadScreen(
    state: QuoteUiState,
    onClose: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(MAX_PHOTOS)
    ) { uris ->
        val room = MAX_PHOTOS - state.selectedPhotoUris.size
        state.selectedPhotoUris.addAll(uris.take(room))
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(SurfaceWhite)) {
            CloseTitleHeader(
                title = "디자인 선택",
                onClose = onClose,
                onNext = if (state.selectedPhotoUris.isNotEmpty()) onNext else null
            )

            Text(
                text = "원하는 네일 디자인 사진을 최대 ${MAX_PHOTOS}장까지 올려주세요",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
            ) {
                items(state.selectedPhotoUris) { uri ->
                    Box(modifier = Modifier.aspectRatio(1f).padding(4.dp)) {
                        AsyncImage(
                            model = uri,
                            contentDescription = "선택한 사진",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(SurfaceWhite)
                                .clickable { state.selectedPhotoUris.remove(uri) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = "제거", modifier = Modifier.size(14.dp))
                        }
                    }
                }
                if (state.selectedPhotoUris.size < MAX_PHOTOS) {
                    item {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MutedRoseBgLight)
                                .border(1.dp, MutedRosePrimary, RoundedCornerShape(8.dp))
                                .clickable {
                                    pickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Filled.AddPhotoAlternate, contentDescription = "사진 추가", tint = MutedRosePrimary)
                                Text(text = "사진 추가", fontSize = 12.sp, color = MutedRosePrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(AppBackground)
                .padding(16.dp)
        ) {
            PrimaryBottomButton(
                text = "다음",
                onClick = onNext,
                enabled = state.selectedPhotoUris.isNotEmpty()
            )
        }
    }
}
