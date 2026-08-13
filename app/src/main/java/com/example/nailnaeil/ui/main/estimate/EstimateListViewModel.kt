package com.example.nailnaeil.ui.main.estimate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.EstimateListItem
import com.example.nailnaeil.data.remote.dto.EstimateStatus
import com.example.nailnaeil.data.repository.EstimateRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstimateListUiState(
    val isLoading: Boolean = true,
    val selectedTabIndex: Int = 0,
    val estimates: List<EstimateListItem> = emptyList(),
    val errorMessage: String? = null
)

class EstimateListViewModel(
    private val repository: EstimateRepository = AppContainer.estimateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EstimateListUiState())
    val uiState: StateFlow<EstimateListUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val status = if (_uiState.value.selectedTabIndex == 0) EstimateStatus.ALL else EstimateStatus.MATCHING
            repository.getEstimates(status, size = 50)
                .onSuccess { response -> _uiState.update { it.copy(isLoading = false, estimates = response.estimates) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }
}
