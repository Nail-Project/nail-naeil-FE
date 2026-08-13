package com.example.nailnaeil.ui.main.address

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nailnaeil.data.remote.dto.UserAddress
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSettingsScreen(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onAddressSelected: (UserAddress) -> Unit
) {
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { AddressState.refresh() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("주소 설정") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Filled.Edit, contentDescription = "주소 편집")
                    }
                }
            )
        }
    ) { padding ->
        val filtered = AddressState.addresses.filter {
            query.isBlank() || it.label.contains(query) || it.address.contains(query)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("동, 읍, 면으로 검색") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp)
                        .clickable {},
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.MyLocation, contentDescription = null, tint = MutedRosePrimary)
                    Text(
                        text = "현재 위치로 찾기",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                HorizontalDivider()

                if (AddressState.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                } else if (AddressState.errorMessage != null) {
                    Text(
                        text = AddressState.errorMessage ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }

            items(filtered) { address ->
                AddressRow(
                    address = address,
                    isCurrent = address.addressId == AddressState.selectedAddressId,
                    onClick = {
                        AddressState.setCurrent(address.addressId)
                        onAddressSelected(address)
                    }
                )
            }
        }
    }
}

@Composable
private fun AddressRow(address: UserAddress, isCurrent: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = address.label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            if (isCurrent) {
                SuggestionChip(
                    onClick = {},
                    label = { Text("현재 설정된 주소", style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Text(
            text = address.address,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
