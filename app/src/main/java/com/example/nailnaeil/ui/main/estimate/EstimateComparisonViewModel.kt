package com.example.nailnaeil.ui.main.estimate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.demo.DemoEngine
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

    /**
     * 데모 요청이면 DemoEngine의 실시간 flow를 구독해서 매장 응답이 도착할 때마다
     * 화면이 자동으로 갱신되게 한다. 실제 서버 요청은 기존처럼 1회성 조회로 처리한다.
     */
    fun load() {
        val demoFlow = DemoEngine.resultFlow(requestId)
        if (demoFlow != null) {
            viewModelScope.launch {
                demoFlow.collect { result -> _uiState.update { it.copy(isLoading = false, result = result, errorMessage = null) } }
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getEstimateResult(requestId)
                .onSuccess { result -> _uiState.update { it.copy(isLoading = false, result = result) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }
}
