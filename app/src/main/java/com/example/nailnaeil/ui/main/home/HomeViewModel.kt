package com.example.nailnaeil.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.DesignSummary
import com.example.nailnaeil.data.remote.dto.EstimateListItem
import com.example.nailnaeil.data.remote.dto.EstimateStatus
import com.example.nailnaeil.data.repository.DesignRepository
import com.example.nailnaeil.data.repository.EstimateRepository
import com.example.nailnaeil.data.repository.UserRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** 무료 요금제는 진행 중인 견적 1개, 플러스 요금제는 최대 3개까지 허용한다는 와이어프레임 가정을 따른다. */
private const val FREE_MAX_ESTIMATES = 1
private const val PLUS_MAX_ESTIMATES = 3

data class HomeUiState(
    val isLoading: Boolean = true,
    val isNPlus: Boolean = false,
    val inProgressEstimates: List<EstimateListItem> = emptyList(),
    val selectedCategory: String = MagazineCategories.first(),
    val designs: List<DesignSummary> = emptyList(),
    val errorMessage: String? = null
) {
    val maxEstimateCount: Int get() = if (isNPlus) PLUS_MAX_ESTIMATES else FREE_MAX_ESTIMATES
    fun needsUpgrade(): Boolean = !isNPlus && inProgressEstimates.size >= FREE_MAX_ESTIMATES
}

class HomeViewModel(
    private val userRepository: UserRepository = AppContainer.userRepository,
    private val estimateRepository: EstimateRepository = AppContainer.estimateRepository,
    private val designRepository: DesignRepository = AppContainer.designRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        loadProfileAndEstimates()
        loadDesigns()
    }

    private fun loadProfileAndEstimates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val profileResult = userRepository.getMe()
            val estimatesResult = estimateRepository.getEstimates(EstimateStatus.MATCHING, size = 10)
            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    isNPlus = profileResult.getOrNull()?.isNPlus ?: state.isNPlus,
                    inProgressEstimates = estimatesResult.getOrNull()?.estimates ?: state.inProgressEstimates,
                    errorMessage = estimatesResult.exceptionOrNull()?.message ?: profileResult.exceptionOrNull()?.message
                )
            }
        }
    }

    private fun loadDesigns() {
        viewModelScope.launch {
            val category = _uiState.value.selectedCategory.takeUnless { it == "전체" }
            designRepository.getDesigns(category = category, size = 8)
                .onSuccess { response -> _uiState.update { it.copy(designs = response.designs.orEmpty()) } }
        }
    }

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadDesigns()
    }

    fun toggleDesignWish(design: DesignSummary) {
        viewModelScope.launch {
            val result = if (design.isBookmarked) {
                designRepository.removeDesignWish(design.designId)
            } else {
                designRepository.addDesignWish(design.designId)
            }
            result.onSuccess { wish ->
                _uiState.update { state ->
                    state.copy(
                        designs = state.designs.map {
                            if (it.designId == design.designId) it.copy(isBookmarked = wish.isBookmarked, wishCount = wish.wishCount) else it
                        }
                    )
                }
            }
        }
    }
}
