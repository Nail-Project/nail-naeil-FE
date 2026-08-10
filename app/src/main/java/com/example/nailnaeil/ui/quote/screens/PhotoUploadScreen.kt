package com.example.nailnaeil.ui.quote.screens

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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nailnaeil.ui.quote.QuoteUiState
import com.example.nailnaeil.ui.quote.components.CloseTitleHeader
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.theme.AppBackground
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextDisabled
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@Composable
fun PhotoUploadScreen(
    state: QuoteUiState,
    onClose: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize().background(SurfaceWhite)) {
        CloseTitleHeader(
            title = "디자인 선택",
            onClose = onClose,
            onNext = if (state.selectedPhotoIds.isNotEmpty()) onNext else null
        )

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable { menuExpanded = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "최근 항목", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "갤러리 선택", tint = TextMain)
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(text = { Text("최근") }, onClick = { menuExpanded = false })
                DropdownMenuItem(text = { Text("즐겨찾기") }, onClick = { menuExpanded = false })
                DropdownMenuItem(text = { Text("모든 사진첩") }, onClick = { menuExpanded = false })
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(1.dp)
                        .background(TextMain),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "카메라", tint = SurfaceWhite)
                }
            }
            items(state.demoGalleryOrdered()) { photo ->
                val order = state.selectedPhotoIds.indexOf(photo.id)
                val selected = order >= 0
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(1.dp)
                        .background(photo.color)
                        .clickable {
                            if (selected) {
                                state.selectedPhotoIds.remove(photo.id)
                            } else if (state.selectedPhotoIds.size < 3) {
                                state.selectedPhotoIds.add(photo.id)
                            }
                            state.syncDesignTags()
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .size(20.dp)
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(if (selected) MutedRosePrimary else SurfaceWhite.copy(alpha = 0.6f))
                            .border(1.dp, SurfaceWhite, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selected) {
                            Text(text = "${order + 1}", color = SurfaceWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                enabled = state.selectedPhotoIds.isNotEmpty()
            )
        }
    }
}

private fun QuoteUiState.demoGalleryOrdered() = com.example.nailnaeil.ui.quote.demoGallery
