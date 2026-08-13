package com.example.nailnaeil.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nailnaeil.data.repository.DesignRepository
import com.example.nailnaeil.ui.main.design.DesignDetailScreen
import com.example.nailnaeil.ui.main.design.DesignDetailViewModel
import com.example.nailnaeil.ui.main.design.RecentProposalsScreen
import com.example.nailnaeil.ui.main.design.RecentProposalsViewModel
import com.example.nailnaeil.ui.main.design.SimilarDesignsScreen
import com.example.nailnaeil.ui.main.design.SimilarDesignsViewModel
import com.example.nailnaeil.ui.main.home.components.MagazineSection
import com.example.nailnaeil.ui.main.my.plan.PlanCompleteScreen
import com.example.nailnaeil.ui.main.my.plan.PlanPaymentScreen
import com.example.nailnaeil.ui.main.my.plan.PlanSelectionScreen
import com.example.nailnaeil.ui.quote.QuoteUiState
import com.example.nailnaeil.ui.quote.screens.TreatmentInfoScreen
import com.example.nailnaeil.ui.theme.SurfaceWhite

/** UI 리뷰용 임시 화면. 목데이터로 최근 작업 화면들을 훑어보기 위한 것으로, 리뷰 끝나면 삭제할 것. */

private enum class PreviewDestination {
    MENU, DESIGN_DETAIL, DESIGN_DETAIL_EMPTY, RECENT_PROPOSALS, SIMILAR_DESIGNS, MAGAZINE_SECTION, TREATMENT_INFO,
    PLAN_SELECTION, PLAN_PAYMENT, PLAN_COMPLETE
}

@Composable
fun UiPreviewScreen() {
    var destination by remember { mutableStateOf(PreviewDestination.MENU) }
    val mockRepo = remember { DesignRepository(FakeDesignApi(empty = false)) }
    val mockRepoEmpty = remember { DesignRepository(FakeDesignApi(empty = true)) }

    when (destination) {
        PreviewDestination.MENU -> PreviewMenu(onSelect = { destination = it })

        PreviewDestination.DESIGN_DETAIL -> DesignDetailScreen(
            designId = 1L,
            onBackClick = { destination = PreviewDestination.MENU },
            onStartEstimate = {},
            viewModel = viewModel(key = "preview_dd") { DesignDetailViewModel(1L, mockRepo) }
        )

        PreviewDestination.DESIGN_DETAIL_EMPTY -> DesignDetailScreen(
            designId = 2L,
            onBackClick = { destination = PreviewDestination.MENU },
            onStartEstimate = {},
            viewModel = viewModel(key = "preview_dd_empty") { DesignDetailViewModel(2L, mockRepoEmpty) }
        )

        PreviewDestination.RECENT_PROPOSALS -> RecentProposalsScreen(
            designId = 1L,
            onBackClick = { destination = PreviewDestination.MENU },
            onRequestEstimate = {},
            viewModel = viewModel(key = "preview_rp") { RecentProposalsViewModel(1L, mockRepo) }
        )

        PreviewDestination.SIMILAR_DESIGNS -> SimilarDesignsScreen(
            designId = 1L,
            onBackClick = { destination = PreviewDestination.MENU },
            onDesignClick = {},
            viewModel = viewModel(key = "preview_sd") { SimilarDesignsViewModel(1L, mockRepo) }
        )

        PreviewDestination.MAGAZINE_SECTION -> MagazineSectionPreview(onBack = { destination = PreviewDestination.MENU })

        PreviewDestination.TREATMENT_INFO -> {
            val state = remember { QuoteUiState() }
            TreatmentInfoScreen(
                state = state,
                onBack = { destination = PreviewDestination.MENU },
                onAddMorePhotos = {},
                onNext = {}
            )
        }

        PreviewDestination.PLAN_SELECTION -> PlanSelectionScreen(
            onBackClick = { destination = PreviewDestination.MENU },
            onSelectNPlus = { destination = PreviewDestination.PLAN_PAYMENT }
        )

        PreviewDestination.PLAN_PAYMENT -> PlanPaymentScreen(
            onBackClick = { destination = PreviewDestination.PLAN_SELECTION },
            onConfirm = { destination = PreviewDestination.PLAN_COMPLETE }
        )

        PreviewDestination.PLAN_COMPLETE -> PlanCompleteScreen(
            onBackClick = { destination = PreviewDestination.PLAN_PAYMENT },
            onConfirm = { destination = PreviewDestination.MENU }
        )
    }
}

@Composable
private fun PreviewMenu(onSelect: (PreviewDestination) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(SurfaceWhite).padding(20.dp)) {
        Text(text = "UI 미리보기 (목데이터)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(
            text = "리뷰용 임시 화면입니다",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(
                listOf(
                    PreviewDestination.DESIGN_DETAIL to "디자인 상세 (데이터 있음)",
                    PreviewDestination.DESIGN_DETAIL_EMPTY to "디자인 상세 (빈 상태)",
                    PreviewDestination.RECENT_PROPOSALS to "최근 견적 사례 전체보기",
                    PreviewDestination.SIMILAR_DESIGNS to "비슷한 디자인 전체보기",
                    PreviewDestination.MAGAZINE_SECTION to "홈 - 디자인 매거진 섹션",
                    PreviewDestination.TREATMENT_INFO to "견적 - 시술 정보 입력",
                    PreviewDestination.PLAN_SELECTION to "마이 - N플러스 요금제 (선택→결제→완료)"
                )
            ) { (dest, label) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .clickable { onSelect(dest) }
                        .padding(16.dp)
                ) {
                    Text(text = label, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MagazineSectionPreview(onBack: () -> Unit) {
    var selectedCategory by remember { mutableStateOf("전체") }
    var items by remember { mutableStateOf(mockDesignSummaries()) }

    Column(modifier = Modifier.fillMaxSize().background(SurfaceWhite).verticalScroll(rememberScrollState())) {
        Text(
            text = "← 메뉴로",
            modifier = Modifier
                .padding(16.dp)
                .clickable(onClick = onBack)
        )
        MagazineSection(
            items = items,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            onLikeToggle = { toggled ->
                items = items.map {
                    if (it.designId == toggled.designId) it.copy(isBookmarked = !it.isBookmarked) else it
                }
            },
            onItemClick = {},
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}