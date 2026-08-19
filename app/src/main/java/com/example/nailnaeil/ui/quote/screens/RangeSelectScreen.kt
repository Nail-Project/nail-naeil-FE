package com.example.nailnaeil.ui.quote.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nailnaeil.data.remote.dto.UserAddress
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.nailnaeil.di.AppContainer
import com.example.nailnaeil.ui.main.address.AddressState
import com.example.nailnaeil.ui.quote.QuoteUiState
import com.example.nailnaeil.ui.quote.SearchRadius
import com.example.nailnaeil.ui.quote.components.BackTitleHeader
import com.example.nailnaeil.ui.quote.components.DotRangeGauge
import com.example.nailnaeil.ui.quote.components.PrimaryBottomButton
import com.example.nailnaeil.ui.quote.components.QuoteProgressBar
import com.example.nailnaeil.ui.quote.hasLocationPermission
import com.example.nailnaeil.ui.quote.lastKnownLocation
import com.example.nailnaeil.ui.theme.BorderLight
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextMain
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSelectScreen(
    state: QuoteUiState,
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAddressPicker by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val location = lastKnownLocation(context)
        if (location == null) {
            state.locationError.value = "위치 정보를 가져올 수 없어요. GPS를 켜주세요."
        } else {
            state.latitude.value = location.latitude
            state.longitude.value = location.longitude
        }
    }

    LaunchedEffect(Unit) {
        if (AddressState.addresses.isEmpty()) AddressState.refresh()
        val selectedAddress = AddressState.selectedAddress
        if (selectedAddress != null) {
            // 주소 설정에서 고른 위치가 있으면 GPS보다 그 위치를 우선한다.
            state.latitude.value = selectedAddress.latitude
            state.longitude.value = selectedAddress.longitude
            state.neighborhood.value = selectedAddress.label
        } else if (hasLocationPermission(context)) {
            val location = lastKnownLocation(context)
            if (location != null) {
                state.latitude.value = location.latitude
                state.longitude.value = location.longitude
            } else {
                state.locationError.value = "위치 정보를 가져올 수 없어요. GPS를 켜주세요."
            }
        } else {
            permissionLauncher.launch(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    LaunchedEffect(state.searchRadius.value, state.latitude.value, state.longitude.value) {
        val lat = state.latitude.value
        val lng = state.longitude.value
        if (lat == null || lng == null) return@LaunchedEffect
        state.isLoadingNearbyShops.value = true
        AppContainer.shopRepository.getNearbyShops(lat, lng, state.searchRadius.value.recommendType)
            .onSuccess { shops ->
                state.nearbyShops.clear()
                state.nearbyShops.addAll(shops)
                state.selectedShopIds.clear()
                state.selectedShopIds.addAll(shops.map { it.shopId }.take(20))
                state.isLoadingNearbyShops.value = false
            }
            .onFailure { e ->
                state.isLoadingNearbyShops.value = false
                state.locationError.value = e.message
            }
    }

    Column(modifier = modifier.fillMaxSize().background(SurfaceWhite)) {
        BackTitleHeader(title = "탐색 범위 지정", onBack = onBack)
        QuoteProgressBar(current = 3, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(text = "견적 받을 네일샵 범위", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { showAddressPicker = true }
            ) {
                Text(
                    text = state.neighborhood.value,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "중심 위치 변경",
                    tint = TextMain,
                    modifier = Modifier.size(18.dp)
                )
            }
            if (state.locationError.value != null) {
                Text(
                    text = state.locationError.value ?: "",
                    fontSize = 12.sp,
                    color = MutedRosePrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(top = 16.dp)) {
            ShopMap(state = state, hasPermission = hasLocationPermission(context))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(SurfaceWhite)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = state.searchRadius.value.headline,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            if (state.isLoadingNearbyShops.value) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp).size(20.dp),
                    strokeWidth = 2.dp,
                    color = MutedRosePrimary
                )
            } else {
                Text(
                    text = "${state.nearbyShops.size}개 매장을 찾았어요 · ${state.searchRadius.value.subtitle}",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp, bottom = 16.dp)
                )
            }
            DotRangeGauge(
                level = SearchRadius.entries.indexOf(state.searchRadius.value),
                total = SearchRadius.entries.size,
                onLevelChange = { level -> state.searchRadius.value = SearchRadius.entries[level] }
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "가까운 네일샵", fontSize = 11.sp, color = TextSecondary)
                Text(text = "먼 네일샵", fontSize = 11.sp, color = TextSecondary)
            }
            PrimaryBottomButton(
                text = state.searchRadius.value.buttonLabel,
                onClick = onSubmit,
                enabled = state.nearbyShops.isNotEmpty(),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    if (showAddressPicker) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(onDismissRequest = { showAddressPicker = false }, sheetState = sheetState) {
            AddressPickerContent(
                addresses = AddressState.addresses,
                currentAddressId = AddressState.selectedAddressId,
                onSelect = { address ->
                    AddressState.setCurrent(address.addressId)
                    state.latitude.value = address.latitude
                    state.longitude.value = address.longitude
                    state.neighborhood.value = address.label
                    showAddressPicker = false
                }
            )
        }
    }
}

@Composable
private fun AddressPickerContent(
    addresses: List<UserAddress>,
    currentAddressId: Long?,
    onSelect: (UserAddress) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text(
            text = "중심 위치 선택",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextMain,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        addresses.forEach { address ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(address) }
                    .padding(vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = address.label,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (address.addressId == currentAddressId) MutedRosePrimary else TextMain
                    )
                    Text(text = address.address, fontSize = 12.sp, color = TextSecondary)
                }
                if (address.addressId == currentAddressId) {
                    Text(text = "선택됨", fontSize = 12.sp, color = MutedRosePrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
        Box(modifier = Modifier.size(1.dp, 12.dp))
    }
}

/**
 * 서울 전체(CITYWIDE, radiusMeters=null)는 검색 반경 자체가 없다는 뜻이라 원은 안 그리지만,
 * 지도가 도시 전체로 지나치게 축소되지 않도록 화면에 보여줄 반경만 WIDE(10km)와 동일하게 맞춘다.
 */
private fun displayRadiusMeters(radiusMeters: Int?): Double = (radiusMeters ?: 10_000).toDouble()

/** 중심점에서 특정 반경(m) 떨어진 사각 영역을 위경도 근사식으로 계산한다. */
private fun boundsForRadius(center: LatLng, radiusMeters: Double): LatLngBounds {
    val latOffset = radiusMeters / 111_320.0
    val lngOffset = radiusMeters / (111_320.0 * kotlin.math.cos(Math.toRadians(center.latitude)).coerceAtLeast(0.1))
    val southwest = LatLng(center.latitude - latOffset, center.longitude - lngOffset)
    val northeast = LatLng(center.latitude + latOffset, center.longitude + lngOffset)
    return LatLngBounds(southwest, northeast)
}

@Composable
private fun ShopMap(state: QuoteUiState, hasPermission: Boolean, modifier: Modifier = Modifier) {
    val lat = state.latitude.value
    val lng = state.longitude.value
    val userLatLng = if (lat != null && lng != null) LatLng(lat, lng) else null
    val radiusMeters = state.searchRadius.value.radiusMeters

    val cameraPositionState = rememberCameraPositionState()
    var isMapLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(userLatLng, radiusMeters, isMapLoaded) {
        if (userLatLng == null) return@LaunchedEffect
        if (!isMapLoaded) {
            // 지도가 아직 레이아웃되기 전이면 bounds fit이 실패할 수 있어 우선 근사 줌으로 이동해둔다.
            cameraPositionState.position = CameraPosition.fromLatLngZoom(userLatLng, 13f)
            return@LaunchedEffect
        }
        val bounds = boundsForRadius(userLatLng, displayRadiusMeters(radiusMeters))
        runCatching {
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 80))
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (userLatLng == null) {
            Box(modifier = Modifier.fillMaxSize().background(BorderLight))
        } else {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = hasPermission),
                uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false),
                onMapLoaded = { isMapLoaded = true }
            ) {
                if (radiusMeters != null) {
                    Circle(
                        center = userLatLng,
                        radius = radiusMeters.toDouble(),
                        fillColor = MutedRosePrimary.copy(alpha = 0.15f),
                        strokeColor = MutedRosePrimary.copy(alpha = 0.5f),
                        strokeWidth = 3f
                    )
                }
                state.nearbyShops.forEach { shop ->
                    MarkerComposable(
                        state = MarkerState(position = LatLng(shop.latitude, shop.longitude)),
                        anchor = androidx.compose.ui.geometry.Offset(0.5f, 1f)
                    ) {
                        ShopMapLabel(name = shop.name)
                    }
                }
            }
        }
    }
}

@Composable
private fun ShopMapLabel(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextMain,
            maxLines = 1,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(SurfaceWhite)
                .border(1.dp, MutedRosePrimary, RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 3.dp)
        )
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(MutedRosePrimary)
                .border(1.dp, SurfaceWhite, CircleShape)
        )
    }
}
