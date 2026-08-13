package com.example.nailnaeil.ui.main.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.ShopAdmin
import com.example.nailnaeil.data.remote.dto.ShopAdminInput
import com.example.nailnaeil.data.repository.AdminShopRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminShopDetailUiState(
    val isLoading: Boolean = true,
    val shop: ShopAdmin? = null,
    val errorMessage: String? = null,
    val isSaving: Boolean = false,
    val saveError: String? = null,
    val saveSuccess: Boolean = false
)

class AdminShopDetailViewModel(
    private val shopId: Long,
    private val repository: AdminShopRepository = AppContainer.adminShopRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminShopDetailUiState())
    val uiState: StateFlow<AdminShopDetailUiState> = _uiState.asStateFlow()

    init {
        loadShop()
    }

    fun loadShop() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getShopDetail(shopId)
                .onSuccess { shop -> _uiState.update { it.copy(isLoading = false, shop = shop) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun save(input: ShopAdminInput) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveError = null, saveSuccess = false) }
            repository.updateShop(shopId, input)
                .onSuccess { shop -> _uiState.update { it.copy(isSaving = false, shop = shop, saveSuccess = true) } }
                .onFailure { e -> _uiState.update { it.copy(isSaving = false, saveError = e.message) } }
        }
    }

    fun consumeSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}
