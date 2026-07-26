package com.example.nailnail.ui.main.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.nailnail.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressEditScreen(
    onBackClick: () -> Unit,
    onModifyClick: (Address) -> Unit
) {
    var deleteTarget by remember { mutableStateOf<Address?>(null) }
    var deleteStep by remember { mutableStateOf<DeleteDialogStep?>(null) }

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            items(AddressMockState.addresses) { address ->
                Column(modifier = Modifier.padding(vertical = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = address.label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        if (address.id == AddressMockState.currentAddressId) {
                            SuggestionChip(
                                onClick = {},
                                label = { Text("현재 설정된 주소", style = MaterialTheme.typography.labelSmall) },
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    Text(
                        text = address.roadAddress,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { onModifyClick(address) }) { Text("수정") }
                        if (address.icon != AddressIconType.HOME) {
                            OutlinedButton(onClick = {
                                deleteTarget = address
                                deleteStep = DeleteDialogStep.CONFIRM
                            }) { Text("삭제") }
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(top = 12.dp))
                }
            }
        }
    }

    val target = deleteTarget
    val step = deleteStep
    if (target != null && step != null) {
        AddressDeleteDialog(
            step = step,
            addressLabel = target.label,
            onCancel = {
                deleteStep = null
                deleteTarget = null
            },
            onConfirmDelete = { deleteStep = DeleteDialogStep.COMPLETE },
            onAcknowledgeComplete = {
                AddressMockState.delete(target.id)
                deleteStep = null
                deleteTarget = null
            }
        )
    }
}
