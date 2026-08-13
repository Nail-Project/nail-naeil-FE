package com.example.nailnaeil.ui.main.design

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.DesignDetailResponse
import com.example.nailnaeil.data.repository.DesignRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DesignDetailUiState(
    val isLoading: Boolean = true,
    val design: DesignDetailResponse? = null,
    val errorMessage: String? = null
)

class DesignDetailViewModel(
    private val designId: Long,
    private val repository: DesignRepository = AppContainer.designRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DesignDetailUiState())
    val uiState: StateFlow<DesignDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getDesignDetail(designId)
                .onSuccess { design -> _uiState.update { it.copy(isLoading = false, design = design) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun toggleWish() {
        val design = _uiState.value.design ?: return
        viewModelScope.launch {
            val result = if (design.isBookmarked) repository.removeDesignWish(designId) else repository.addDesignWish(designId)
            result.onSuccess { wish ->
                _uiState.update { state ->
                    state.copy(design = state.design?.copy(isBookmarked = wish.isBookmarked, wishCount = wish.wishCount))
                }
            }
        }
    }
}
