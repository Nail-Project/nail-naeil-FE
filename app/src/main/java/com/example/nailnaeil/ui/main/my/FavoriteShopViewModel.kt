package com.example.nailnaeil.ui.main.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.ShopSummary
import com.example.nailnaeil.data.repository.ShopRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoriteShopUiState(
    val isLoading: Boolean = true,
    val shops: List<ShopSummary> = emptyList(),
    val errorMessage: String? = null
)

class FavoriteShopViewModel(
    private val shopRepository: ShopRepository = AppContainer.shopRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteShopUiState())
    val uiState: StateFlow<FavoriteShopUiState> = _uiState.asStateFlow()

    init {
        loadBookmarkedShops()
    }

    fun loadBookmarkedShops() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            shopRepository.getBookmarkedShops()
                .onSuccess { response -> _uiState.update { it.copy(isLoading = false, shops = response.shops) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }
}
