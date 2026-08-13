package com.example.nailnaeil.ui.main.design

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.DesignProposal
import com.example.nailnaeil.data.repository.DesignRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RecentProposalsUiState(
    val isLoading: Boolean = true,
    val ascending: Boolean = true,
    val proposals: List<DesignProposal> = emptyList(),
    val errorMessage: String? = null
)

/**
 * 디자인 상세 응답의 recentProposals를 그대로 재사용한다.
 * 백엔드에 별도의 "견적 사례 전체 목록" API가 없어, 상세 조회로 받은 목록 안에서만 정렬한다.
 */
class RecentProposalsViewModel(
    private val designId: Long,
    private val repository: DesignRepository = AppContainer.designRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecentProposalsUiState())
    val uiState: StateFlow<RecentProposalsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getDesignDetail(designId)
                .onSuccess { design ->
                    val proposals = design.recentProposals.orEmpty()
                    _uiState.update { it.copy(isLoading = false, proposals = sorted(proposals, it.ascending)) }
                }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun toggleSortOrder() {
        _uiState.update { it.copy(ascending = !it.ascending, proposals = sorted(it.proposals, !it.ascending)) }
    }

    private fun sorted(proposals: List<DesignProposal>, ascending: Boolean): List<DesignProposal> {
        return if (ascending) proposals.sortedBy { it.price } else proposals.sortedByDescending { it.price }
    }
}
