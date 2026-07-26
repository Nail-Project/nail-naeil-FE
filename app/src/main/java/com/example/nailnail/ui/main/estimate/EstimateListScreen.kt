package com.example.nailnail.ui.main.estimate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.nailnail.ui.theme.TextSecondary

@Composable
fun EstimateListScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "견적함 화면 준비 중입니다",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
    }
}
