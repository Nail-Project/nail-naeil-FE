package com.example.nailnaeil.ui.main.admin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nailnaeil.data.remote.dto.ShopAdmin
import com.example.nailnaeil.data.remote.dto.ShopAdminInput
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminShopDetailScreen(
    shopId: Long,
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    viewModel: AdminShopDetailViewModel = viewModel { AdminShopDetailViewModel(shopId) }
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val shop = uiState.shop

    var name by remember(shop) { mutableStateOf(shop?.name ?: "") }
    var phoneNumber by remember(shop) { mutableStateOf(shop?.phoneNumber ?: "") }
    var address by remember(shop) { mutableStateOf(shop?.address ?: "") }
    var addressDetail by remember(shop) { mutableStateOf(shop?.addressDetail ?: "") }
    var districtName by remember(shop) { mutableStateOf(shop?.districtName ?: "") }
    var adminDongName by remember(shop) { mutableStateOf(shop?.adminDongName ?: "") }
    var locationGuide by remember(shop) { mutableStateOf(shop?.locationGuide ?: "") }
    var parkingInfo by remember(shop) { mutableStateOf(shop?.parkingInfo ?: "") }
    var thumbnailImageUrl by remember(shop) { mutableStateOf(shop?.thumbnailImageUrl ?: "") }
    var closedDaysText by remember(shop) { mutableStateOf(shop?.closedDays?.joinToString(", ") ?: "") }
    var latitudeText by remember(shop) { mutableStateOf(shop?.latitude?.toString() ?: "") }
    var longitudeText by remember(shop) { mutableStateOf(shop?.longitude?.toString() ?: "") }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            viewModel.consumeSaveSuccess()
            Toast.makeText(context, "저장했어요.", Toast.LENGTH_SHORT).show()
            onSaved()
        }
    }

    LaunchedEffect(uiState.saveError) {
        uiState.saveError?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("매장 상세") },
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
                shop == null -> Text(
                    text = uiState.errorMessage ?: "매장 정보를 불러오지 못했어요.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    ShopStatsHeader(shop)

                    AdminField(label = "매장명", value = name, onValueChange = { name = it })
                    AdminField(label = "전화번호", value = phoneNumber, onValueChange = { phoneNumber = it }, keyboardType = KeyboardType.Phone)
                    AdminField(label = "주소", value = address, onValueChange = { address = it })
                    AdminField(label = "상세주소", value = addressDetail, onValueChange = { addressDetail = it })
                    AdminField(label = "구", value = districtName, onValueChange = { districtName = it })
                    AdminField(label = "동", value = adminDongName, onValueChange = { adminDongName = it })
                    AdminField(label = "위치 안내", value = locationGuide, onValueChange = { locationGuide = it })
                    AdminField(label = "주차 안내", value = parkingInfo, onValueChange = { parkingInfo = it })
                    AdminField(label = "대표 이미지 URL", value = thumbnailImageUrl, onValueChange = { thumbnailImageUrl = it })
                    AdminField(label = "휴무일 (쉼표로 구분, 예: 매주 화요일)", value = closedDaysText, onValueChange = { closedDaysText = it })
                    AdminField(label = "위도", value = latitudeText, onValueChange = { latitudeText = it }, keyboardType = KeyboardType.Decimal)
                    AdminField(label = "경도", value = longitudeText, onValueChange = { longitudeText = it }, keyboardType = KeyboardType.Decimal)

                    Row(modifier = Modifier.padding(top = 32.dp)) {
                        OutlinedButton(onClick = onBackClick, modifier = Modifier.weight(1f)) {
                            Text("취소")
                        }
                        Button(
                            onClick = {
                                val lat = latitudeText.toDoubleOrNull()
                                val lng = longitudeText.toDoubleOrNull()
                                if (lat == null || lng == null) {
                                    Toast.makeText(context, "위도/경도를 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.save(
                                    ShopAdminInput(
                                        name = name,
                                        phoneNumber = phoneNumber.ifBlank { null },
                                        address = address,
                                        addressDetail = addressDetail.ifBlank { null },
                                        districtName = districtName.ifBlank { null },
                                        adminDongName = adminDongName.ifBlank { null },
                                        latitude = lat,
                                        longitude = lng,
                                        locationGuide = locationGuide.ifBlank { null },
                                        parkingInfo = parkingInfo.ifBlank { null },
                                        thumbnailImageUrl = thumbnailImageUrl.ifBlank { null },
                                        closedDays = closedDaysText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                            .ifEmpty { null }
                                    )
                                )
                            },
                            enabled = !uiState.isSaving,
                            colors = ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {
                            Text(if (uiState.isSaving) "저장 중..." else "저장", color = SurfaceWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShopStatsHeader(shop: ShopAdmin) {
    Row(modifier = Modifier.padding(bottom = 20.dp)) {
        AsyncImage(
            model = shop.thumbnailImageUrl,
            contentDescription = shop.name,
            modifier = Modifier
                .height(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = shop.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                text = "★ ${String.format("%.1f", shop.rating)} (${shop.reviewCount})" +
                    if (!shop.isDataActive) " · 비활성 매장" else "",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun AdminField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        )
    }
}
