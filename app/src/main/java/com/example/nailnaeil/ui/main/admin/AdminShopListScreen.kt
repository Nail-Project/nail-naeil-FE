package com.example.nailnaeil.ui.main.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.ShopAdmin
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminShopListScreen(
    onBackClick: () -> Unit,
    onShopClick: (Long) -> Unit,
    viewModel: AdminShopListViewModel = viewModel { AdminShopListViewModel() }
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showSyncDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.syncResultMessage) {
        uiState.syncResultMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.consumeSyncResultMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("매장 관리") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { showSyncDialog = true }, enabled = !uiState.isSyncing) {
                        if (uiState.isSyncing) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Filled.Sync, contentDescription = "네일샵 데이터 동기화")
                        }
                    }
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
            OutlinedTextField(
                value = uiState.keyword,
                onValueChange = { viewModel.setKeyword(it) },
                placeholder = { Text("매장 검색") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    uiState.errorMessage != null && uiState.shops.isEmpty() -> Text(
                        text = uiState.errorMessage ?: "",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                    else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.filteredShops) { shop ->
                            AdminShopRow(
                                shop = shop,
                                isSelectMode = uiState.isSelectMode,
                                isSelected = shop.shopId in uiState.selectedShopIds,
                                onClick = {
                                    if (uiState.isSelectMode) viewModel.toggleSelected(shop.shopId) else onShopClick(shop.shopId)
                                }
                            )
                        }
                    }
                }
            }

            if (uiState.isSelectMode && uiState.selectedShopIds.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceWhite)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${uiState.selectedShopIds.size}개의 매장 선택됨",
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
        val firstName = uiState.shops.firstOrNull { it.shopId in uiState.selectedShopIds }?.name ?: ""
        val extraCount = uiState.selectedShopIds.size - 1
        AdminDeleteConfirmDialog(
            message = if (extraCount > 0) "'$firstName' 외 ${extraCount}건" else "'$firstName'",
            description = "매장을 삭제할까요?",
            onDismiss = { showDeleteConfirm = false },
            onConfirm = {
                showDeleteConfirm = false
                viewModel.deleteSelected {}
            }
        )
    }

    if (showSyncDialog) {
        AdminShopSyncDialog(
            onDismiss = { showSyncDialog = false },
            onConfirm = { industryCode, pageSize, maxPages ->
                showSyncDialog = false
                viewModel.syncShops(industryCode, pageSize, maxPages)
            }
        )
    }
}

@Composable
private fun AdminShopSyncDialog(
    onDismiss: () -> Unit,
    onConfirm: (industryCode: String, pageSize: Int?, maxPages: Int?) -> Unit
) {
    var industryCode by remember { mutableStateOf("") }
    var pageSize by remember { mutableStateOf("") }
    var maxPages by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(SurfaceWhite, RoundedCornerShape(12.dp))
                .padding(24.dp)
        ) {
            Text(text = "네일샵 데이터 동기화", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                text = "소상공인 상가정보 API에서 지정한 업종 코드의 네일샵 데이터를 불러와요.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
            OutlinedTextField(
                value = industryCode,
                onValueChange = { industryCode = it },
                singleLine = true,
                label = { Text("상권업종 소분류 코드") },
                placeholder = { Text("예: I2015") },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )
            OutlinedTextField(
                value = pageSize,
                onValueChange = { pageSize = it.filter(Char::isDigit) },
                singleLine = true,
                label = { Text("페이지당 개수 (선택, 최대 1000)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )
            OutlinedTextField(
                value = maxPages,
                onValueChange = { maxPages = it.filter(Char::isDigit) },
                singleLine = true,
                label = { Text("최대 페이지 수 (선택, 최대 100)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )
            Row(modifier = Modifier.padding(top = 20.dp)) {
                TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("취소") }
                TextButton(
                    onClick = { onConfirm(industryCode, pageSize.toIntOrNull(), maxPages.toIntOrNull()) },
                    enabled = industryCode.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("동기화", color = MutedRosePrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AdminShopRow(shop: ShopAdmin, isSelectMode: Boolean, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelectMode && isSelected) MutedRoseBgLight else SurfaceWhite)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = shop.thumbnailImageUrl,
            contentDescription = shop.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = shop.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                text = shop.address,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Text(text = "★", color = androidx.compose.ui.graphics.Color(0xFFFFAA00), style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = " ${String.format("%.1f", shop.rating)} (${shop.reviewCount})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                if (!shop.isDataActive) {
                    Text(
                        text = "  비활성",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedRosePrimary
                    )
                }
            }
        }
    }
}

@Composable
fun AdminDeleteConfirmDialog(message: String, description: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(SurfaceWhite, RoundedCornerShape(12.dp))
                .padding(24.dp)
        ) {
            Text(text = message, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
            Row(modifier = Modifier.padding(top = 24.dp)) {
                TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("이전으로") }
                TextButton(onClick = onConfirm, modifier = Modifier.weight(1f)) {
                    Text("삭제", color = MutedRosePrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
