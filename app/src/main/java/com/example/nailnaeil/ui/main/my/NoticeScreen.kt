package com.example.nailnaeil.ui.main.my

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.window.Dialog
import com.example.nailnaeil.ui.theme.MutedRoseBgLight
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeScreen(onBackClick: () -> Unit) {
    var selectedNotice by remember { mutableStateOf<NoticeItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("공지사항") },
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
        ) {
            items(MockNotices) { notice ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (notice.isHighlighted) MutedRoseBgLight else MaterialTheme.colorScheme.surface)
                        .clickable { selectedNotice = notice }
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = notice.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text(
                            text = notice.date,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                    Icon(Icons.Filled.ChevronRight, contentDescription = "공지사항 상세 보기", tint = TextSecondary)
                }
            }
        }
    }

    selectedNotice?.let { notice ->
        Dialog(onDismissRequest = { selectedNotice = null }) {
            Column(
                modifier = Modifier
                    .background(SurfaceWhite)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = notice.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    text = notice.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = notice.content,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    }
}
