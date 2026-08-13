package com.example.nailnaeil.ui.main.admin

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.nailnaeil.data.remote.dto.DesignSummary
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMagazineListScreen(
    onBackClick: () -> Unit,
    onDesignClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    viewModel: AdminMagazineListViewModel = viewModel { AdminMagazineListViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var newCategoryInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("매거진 관리") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    if (uiState.isSelectMode) {
                        IconButton(onClick = { viewModel.toggleSelectMode() }) {
                            Icon(Icons.Filled.Close, contentDescription = "선택 취소")
                        }
                    } else {
                        TextButton(onClick = { viewModel.toggleSelectMode() }) {
                            Text("선택", color = MutedRosePrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.toggleCategoryEditMode() }) {
                    Icon(Icons.Filled.Edit, contentDescription = "카테고리 수정", tint = if (uiState.isCategoryEditMode) MutedRosePrimary else TextSecondary)
                }
                LazyRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(end = 12.dp)
                ) {
                    items(uiState.categories) { category ->
                        CategoryChip(
                            label = category,
                            selected = category == uiState.selectedCategory,
                            editMode = uiState.isCategoryEditMode && category != "전체",
                            onClick = { if (!uiState.isCategoryEditMode) viewModel.selectCategory(category) },
                            onRemove = { viewModel.removeCategory(category) }
                        )
                    }
                }
            }

            if (uiState.isCategoryEditMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newCategoryInput,
                        onValueChange = { newCategoryInput = it },
                        placeholder = { Text("새 카테고리 이름") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = {
                        viewModel.addCategory(newCategoryInput.trim())
                        newCategoryInput = ""
                    }) {
                        Text("추가", fontWeight = FontWeight.Bold)
                    }
                }
                Text(
                    text = "※ 카테고리는 이 기기에서만 유지돼요. 서버에 저장하는 기능은 아직 없어요.",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    uiState.errorMessage != null && uiState.designs.isEmpty() -> Text(
                        text = uiState.errorMessage ?: "",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                    else -> LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (!uiState.isSelectMode) {
                            item { AddMagazineTile(onClick = onAddClick) }
                        }
                        items(uiState.designs) { design ->
                            AdminMagazineCard(
                                design = design,
                                isSelectMode = uiState.isSelectMode,
                                isSelected = design.designId in uiState.selectedDesignIds,
                                onClick = {
                                    if (uiState.isSelectMode) viewModel.toggleSelected(design.designId) else onDesignClick(design.designId)
                                }
                            )
                        }
                    }
                }
            }

            if (uiState.isSelectMode && uiState.selectedDesignIds.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceWhite)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${uiState.selectedDesignIds.size}개의 매거진 선택됨",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    FloatingActionButton(
                        onClick = { showDeleteConfirm = true },
                        containerColor = MutedRosePrimary,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "선택 삭제", tint = SurfaceWhite)
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        val firstTitle = uiState.designs.firstOrNull { it.designId in uiState.selectedDesignIds }?.title ?: ""
        val extraCount = uiState.selectedDesignIds.size - 1
        AdminDeleteConfirmDialog(
            message = if (extraCount > 0) "'$firstTitle' 외 ${extraCount}건" else "'$firstTitle'",
            description = "매거진을 삭제할까요?",
            onDismiss = { showDeleteConfirm = false },
            onConfirm = {
                showDeleteConfirm = false
                viewModel.deleteSelected()
            }
        )
    }
}

@Composable
private fun CategoryChip(label: String, selected: Boolean, editMode: Boolean, onClick: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MutedRosePrimary else MutedRoseBgLight)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) SurfaceWhite else TextSecondary,
            fontWeight = FontWeight.Bold
        )
        if (editMode) {
            Icon(
                Icons.Filled.RemoveCircle,
                contentDescription = "$label 삭제",
                tint = if (selected) SurfaceWhite else MutedRosePrimary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(16.dp)
                    .clickable(onClick = onRemove)
            )
        }
    }
}

@Composable
private fun AddMagazineTile(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.83f)
            .clip(RoundedCornerShape(8.dp))
            .background(MutedRoseBgLight)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.Add, contentDescription = "매거진 등록하기", tint = MutedRosePrimary)
        Text(
            text = "매거진 등록하기",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedRosePrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun AdminMagazineCard(design: DesignSummary, isSelectMode: Boolean, isSelected: Boolean, onClick: () -> Unit) {
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
        if (isSelectMode && isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000))
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            Text(
                text = design.title,
                style = MaterialTheme.typography.bodyMedium,
                color = SurfaceWhite,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Icon(Icons.Filled.Visibility, contentDescription = null, tint = SurfaceWhite, modifier = Modifier.size(14.dp))
                Text(
                    text = " ${design.viewCount}  ",
                    style = MaterialTheme.typography.labelSmall,
                    color = SurfaceWhite
                )
                Icon(Icons.Filled.Favorite, contentDescription = null, tint = SurfaceWhite, modifier = Modifier.size(14.dp))
                Text(text = " ${design.wishCount}", style = MaterialTheme.typography.labelSmall, color = SurfaceWhite)
            }
        }
    }
}
