package com.example.nailnaeil.ui.main.estimate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nailnaeil.data.remote.dto.EstimateListItem
import com.example.nailnaeil.ui.main.estimate.components.EstimateCard
import com.example.nailnaeil.ui.theme.MutedRosePrimary
import com.example.nailnaeil.ui.theme.SurfaceWhite
import com.example.nailnaeil.ui.theme.TextDisabled
import com.example.nailnaeil.ui.theme.TextSecondary

private val EstimateTabs = listOf("전체", "진행중")

@Composable
fun EstimateListScreen(
    onItemClick: (EstimateListItem) -> Unit,
    viewModel: EstimateListViewModel = viewModel { EstimateListViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
    ) {
        Text(
            text = "견적함",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        SecondaryTabRow(selectedTabIndex = uiState.selectedTabIndex, contentColor = MutedRosePrimary) {
            EstimateTabs.forEachIndexed { index, title ->
                Tab(
                    selected = uiState.selectedTabIndex == index,
                    onClick = { viewModel.selectTab(index) },
                    text = { Text(title) },
                    selectedContentColor = MutedRosePrimary,
                    unselectedContentColor = TextDisabled
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                uiState.errorMessage != null && uiState.estimates.isEmpty() -> Text(
                    text = uiState.errorMessage ?: "",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
                uiState.estimates.isEmpty() -> Text(
                    text = "요청한 견적이 없어요.",
                    color = TextSecondary,
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.estimates, key = { it.estimateId }) { item ->
                        EstimateCard(item = item, onClick = { onItemClick(item) })
                    }
                }
            }
        }
    }
}
