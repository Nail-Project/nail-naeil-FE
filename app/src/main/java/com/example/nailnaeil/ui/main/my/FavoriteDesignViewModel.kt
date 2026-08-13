package com.example.nailnaeil.ui.main.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.BookmarkedDesignItem
import com.example.nailnaeil.data.repository.DesignRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoriteDesignUiState(
    val isLoading: Boolean = true,
    val designs: List<BookmarkedDesignItem> = emptyList(),
    val errorMessage: String? = null
)

class FavoriteDesignViewModel(
    private val designRepository: DesignRepository = AppContainer.designRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteDesignUiState())
    val uiState: StateFlow<FavoriteDesignUiState> = _uiState.asStateFlow()

    init {
        loadWishlist()
    }

    fun loadWishlist() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            designRepository.getDesignWishlist()
                .onSuccess { response -> _uiState.update { it.copy(isLoading = false, designs = response.designs) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun removeWish(designId: Long) {
        viewModelScope.launch {
            designRepository.removeDesignWish(designId)
                .onSuccess {
                    _uiState.update { state -> state.copy(designs = state.designs.filterNot { it.designId == designId }) }
                }
        }
    }
}
