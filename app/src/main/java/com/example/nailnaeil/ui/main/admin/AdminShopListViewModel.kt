package com.example.nailnaeil.ui.main.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.ShopAdmin
import com.example.nailnaeil.data.repository.AdminShopRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminShopListUiState(
    val isLoading: Boolean = true,
    val shops: List<ShopAdmin> = emptyList(),
    val errorMessage: String? = null,
    val keyword: String = "",
    val isSelectMode: Boolean = false,
    val selectedShopIds: Set<Long> = emptySet(),
    val isDeleting: Boolean = false,
    val isSyncing: Boolean = false,
    val syncResultMessage: String? = null
) {
    val filteredShops: List<ShopAdmin>
        get() = if (keyword.isBlank()) {
            shops
        } else {
            shops.filter { it.name.contains(keyword, ignoreCase = true) || it.address.contains(keyword, ignoreCase = true) }
        }
}

class AdminShopListViewModel(
    private val repository: AdminShopRepository = AppContainer.adminShopRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminShopListUiState())
    val uiState: StateFlow<AdminShopListUiState> = _uiState.asStateFlow()

    init {
        loadShops()
    }

    fun loadShops() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getShops(limit = 100, active = "all")
                .onSuccess { response -> _uiState.update { it.copy(isLoading = false, shops = response.shops) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun syncShops(industryCode: String, pageSize: Int?, maxPages: Int?) {
        if (industryCode.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true, syncResultMessage = null) }
            repository.syncShops(industryCode, pageSize, maxPages)
                .onSuccess {
                    _uiState.update { it.copy(isSyncing = false, syncResultMessage = "동기화가 완료됐어요.") }
                    loadShops()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isSyncing = false, syncResultMessage = e.message ?: "동기화에 실패했어요.") }
                }
        }
    }

    fun consumeSyncResultMessage() {
        _uiState.update { it.copy(syncResultMessage = null) }
    }

    fun setKeyword(keyword: String) {
        _uiState.update { it.copy(keyword = keyword) }
    }

    fun toggleSelectMode() {
        _uiState.update { it.copy(isSelectMode = !it.isSelectMode, selectedShopIds = emptySet()) }
    }

    fun toggleSelected(shopId: Long) {
        _uiState.update { state ->
            val next = if (shopId in state.selectedShopIds) state.selectedShopIds - shopId else state.selectedShopIds + shopId
            state.copy(selectedShopIds = next)
        }
    }

    fun deleteSelected(onComplete: () -> Unit) {
        val ids = _uiState.value.selectedShopIds
        if (ids.isEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            var lastError: String? = null
            ids.forEach { id ->
                repository.deactivateShop(id).onFailure { e -> lastError = e.message }
            }
            _uiState.update {
                it.copy(
                    isDeleting = false,
                    isSelectMode = false,
                    selectedShopIds = emptySet(),
                    errorMessage = lastError
                )
            }
            loadShops()
            onComplete()
        }
    }
}
