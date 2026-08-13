package com.example.nailnaeil.ui.main.address

import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.data.remote.dto.UserAddressUpdateRequest
import com.example.nailnaeil.di.AppContainer
import com.example.nailnaeil.ui.quote.hasLocationPermission
import com.example.nailnaeil.ui.quote.lastKnownLocation
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

private val DEFAULT_SEOUL = LatLng(37.5665, 126.9780)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressFormScreen(
    addressId: String,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val isNew = addressId == "new"
    val existing = remember(addressId) { if (isNew) null else AddressState.findById(addressId.toLongOrNull() ?: -1L) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var label by remember { mutableStateOf(existing?.label.orEmpty()) }
    var detailAddress by remember { mutableStateOf(existing?.addressDetail.orEmpty()) }
    var isDefault by remember { mutableStateOf(existing?.isDefault ?: false) }
    var isSaving by remember { mutableStateOf(false) }

    var pickedAddress by remember { mutableStateOf(existing?.address.orEmpty()) }
    var pickedLatLng by remember {
        mutableStateOf(existing?.let { LatLng(it.latitude, it.longitude) })
    }
    var isGeocoding by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pickedLatLng ?: DEFAULT_SEOUL, 16f)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val loc = lastKnownLocation(context)
        if (loc != null) cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(loc.latitude, loc.longitude), 16f)
    }

    LaunchedEffect(Unit) {
        if (isNew && pickedLatLng == null) {
            if (hasLocationPermission(context)) {
                val loc = lastKnownLocation(context)
                if (loc != null) cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(loc.latitude, loc.longitude), 16f)
            } else {
                permissionLauncher.launch(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        }
    }

    // 지도 이동이 멈추면 중심 좌표를 역지오코딩해서 주소 텍스트를 채운다.
    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving) return@LaunchedEffect
        val center = cameraPositionState.position.target
        pickedLatLng = center
        isGeocoding = true
        val resolved = withContext(Dispatchers.IO) {
            runCatching {
                @Suppress("DEPRECATION")
                Geocoder(context, Locale.KOREA).getFromLocation(center.latitude, center.longitude, 1)
                    ?.firstOrNull()?.getAddressLine(0)
            }.getOrNull()
        }
        if (resolved != null) pickedAddress = resolved
        isGeocoding = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNew) "새 주소 추가" else "주소 편집") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
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
            Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = hasLocationPermission(context)),
                    uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
                )
                // 지도를 움직이면 그 아래 지도가 스크롤되고, 화면 중앙의 이 핀이 실제 선택 좌표가 된다.
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "선택 위치",
                    tint = MutedRosePrimary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 32.dp)
                        .size(36.dp)
                )
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Place, contentDescription = null, tint = MutedRosePrimary)
                    if (isGeocoding) {
                        CircularProgressIndicator(modifier = Modifier.padding(start = 8.dp).size(16.dp), strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = pickedAddress.ifBlank { "지도를 움직여 위치를 선택해주세요" },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    placeholder = { Text("주소 이름 (예: 우리집, 회사)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                OutlinedTextField(
                    value = detailAddress,
                    onValueChange = { detailAddress = it },
                    placeholder = { Text("상세 주소 입력 (선택)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )

                FilterChip(
                    selected = isDefault,
                    onClick = { isDefault = !isDefault },
                    label = { Text("기본 주소로 설정") },
                    modifier = Modifier.padding(top = 16.dp)
                )

                Button(
                    onClick = {
                        val latLng = pickedLatLng ?: return@Button
                        if (isSaving) return@Button
                        isSaving = true
                        scope.launch {
                            val result = if (isNew) {
                                AppContainer.userRepository.addAddress(
                                    label = label,
                                    address = pickedAddress,
                                    addressDetail = detailAddress.ifBlank { null },
                                    latitude = latLng.latitude,
                                    longitude = latLng.longitude,
                                    isDefault = isDefault
                                )
                            } else {
                                AppContainer.userRepository.updateAddress(
                                    existing!!.addressId,
                                    UserAddressUpdateRequest(
                                        label = label.ifBlank { null },
                                        address = pickedAddress.ifBlank { null },
                                        addressDetail = detailAddress,
                                        latitude = latLng.latitude,
                                        longitude = latLng.longitude,
                                        isDefault = isDefault
                                    )
                                )
                            }
                            result
                                .onSuccess {
                                    AddressState.refresh()
                                    isSaving = false
                                    onSaved()
                                }
                                .onFailure { isSaving = false }
                        }
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MutedRosePrimary),
                    enabled = !isSaving && !isGeocoding && label.isNotBlank() && pickedAddress.isNotBlank() && pickedLatLng != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = SurfaceWhite)
                    } else {
                        Text(if (isNew) "주소 등록하기" else "저장하기")
                    }
                }
            }
        }
    }
}
