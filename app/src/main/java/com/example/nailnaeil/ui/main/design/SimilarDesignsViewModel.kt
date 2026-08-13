package com.example.nailnaeil.ui.main.design

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.DesignSummary
import com.example.nailnaeil.data.repository.DesignRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SimilarDesignsUiState(
    val isLoading: Boolean = true,
    val designs: List<DesignSummary> = emptyList(),
    val errorMessage: String? = null
)

/**
 * 백엔드에 "이 디자인과 비슷한 디자인" 전용 API가 없어, 디자인 매거진 전체 목록을 그대로 보여준다.
 * 추후 유사도 기반 API가 추가되면 designId로 필터링하도록 바꾼다.
 */
class SimilarDesignsViewModel(
    private val designId: Long,
    private val repository: DesignRepository = AppContainer.designRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SimilarDesignsUiState())
    val uiState: StateFlow<SimilarDesignsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getDesigns(size = 30)
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(isLoading = false, designs = response.designs.orEmpty().filter { design -> design.designId != designId })
                    }
                }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun toggleWish(design: DesignSummary) {
        viewModelScope.launch {
            val result = if (design.isBookmarked) {
                repository.removeDesignWish(design.designId)
            } else {
                repository.addDesignWish(design.designId)
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
