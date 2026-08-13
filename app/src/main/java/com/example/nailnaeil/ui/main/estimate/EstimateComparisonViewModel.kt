package com.example.nailnaeil.ui.main.estimate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.EstimateResultResponse
import com.example.nailnaeil.data.repository.EstimateRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstimateComparisonUiState(
    val isLoading: Boolean = true,
    val result: EstimateResultResponse? = null,
    val errorMessage: String? = null
)

class EstimateComparisonViewModel(
    private val requestId: Long,
    private val repository: EstimateRepository = AppContainer.estimateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EstimateComparisonUiState())
    val uiState: StateFlow<EstimateComparisonUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getEstimateResult(requestId)
                .onSuccess { result -> _uiState.update { it.copy(isLoading = false, result = result) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }
}
