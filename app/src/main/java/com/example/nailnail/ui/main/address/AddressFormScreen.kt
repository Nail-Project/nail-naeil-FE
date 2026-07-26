package com.example.nailnail.ui.main.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnail.ui.theme.MutedRosePrimary
import com.example.nailnail.ui.theme.PhotoPlaceholderGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressFormScreen(
    addressId: String,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val existing = remember(addressId) { AddressMockState.findById(addressId) }
    var detailAddress by remember { mutableStateOf(existing?.detailAddress.orEmpty()) }
    var selectedIcon by remember { mutableStateOf(existing?.icon ?: AddressIconType.OTHER) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("주소 편집") },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(PhotoPlaceholderGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Map, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                Text(
                    text = "지도 영역 (Maps SDK 연동 필요)",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 40.dp)
                )
            }

            Column(modifier = Modifier.padding(20.dp)) {
                if (existing?.id == AddressMockState.currentAddressId) {
                    SuggestionChip(onClick = {}, label = { Text("현재 설정된 주소") })
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    Icon(Icons.Filled.Place, contentDescription = null, tint = MutedRosePrimary)
                    Text(
                        text = existing?.roadAddress ?: "새 주소 검색",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = detailAddress,
                    onValueChange = { detailAddress = it },
                    placeholder = { Text("상세 주소 입력 (선택)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedIcon == AddressIconType.HOME,
                        onClick = { selectedIcon = AddressIconType.HOME },
                        leadingIcon = { Icon(Icons.Filled.Home, contentDescription = null) },
                        label = { Text("우리집") }
                    )
                    FilterChip(
                        selected = selectedIcon == AddressIconType.WORK,
                        onClick = { selectedIcon = AddressIconType.WORK },
                        leadingIcon = { Icon(Icons.Filled.Business, contentDescription = null) },
                        label = { Text("회사") }
                    )
                    FilterChip(
                        selected = selectedIcon == AddressIconType.OTHER,
                        onClick = { selectedIcon = AddressIconType.OTHER },
                        leadingIcon = { Icon(Icons.Filled.Place, contentDescription = null) },
                        label = { Text("다른 위치") }
                    )
                }

                Button(
                    onClick = {
                        val label = when (selectedIcon) {
                            AddressIconType.HOME -> "우리집"
                            AddressIconType.WORK -> "회사"
                            AddressIconType.OTHER -> existing?.label ?: "새 주소"
                        }
                        AddressMockState.upsert(
                            Address(
                                id = existing?.id ?: addressId,
                                label = label,
                                roadAddress = existing?.roadAddress ?: "새로 등록한 주소",
                                detailAddress = detailAddress,
                                icon = selectedIcon
                            )
                        )
                        onSaved()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text(if (existing != null) "저장하기" else "주소 등록하기")
                }
            }
        }
    }
}
