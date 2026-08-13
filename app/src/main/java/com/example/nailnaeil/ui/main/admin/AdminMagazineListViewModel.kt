package com.example.nailnaeil.ui.main.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.DesignSummary
import com.example.nailnaeil.data.repository.AdminDesignRepository
import com.example.nailnaeil.data.repository.DesignRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val DEFAULT_CATEGORIES = listOf("전체", "인기", "심플", "마그넷", "프렌치", "파츠", "아트")

data class AdminMagazineListUiState(
    val isLoading: Boolean = true,
    val designs: List<DesignSummary> = emptyList(),
    val errorMessage: String? = null,
    /** 카테고리 탭 목록. 이를 관리하는 백엔드 API가 없어 세션 동안만 유지되는 로컬 상태다(재실행 시 초기화됨). */
    val categories: List<String> = DEFAULT_CATEGORIES,
    val selectedCategory: String = DEFAULT_CATEGORIES.first(),
    val isCategoryEditMode: Boolean = false,
    val isSelectMode: Boolean = false,
    val selectedDesignIds: Set<Long> = emptySet(),
    val isDeleting: Boolean = false
)

class AdminMagazineListViewModel(
    private val designRepository: DesignRepository = AppContainer.designRepository,
    private val adminDesignRepository: AdminDesignRepository = AppContainer.adminDesignRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminMagazineListUiState())
    val uiState: StateFlow<AdminMagazineListUiState> = _uiState.asStateFlow()

    init {
        loadDesigns()
    }

    fun loadDesigns() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val category = _uiState.value.selectedCategory.takeUnless { it == "전체" }
            designRepository.getDesigns(category = category, size = 60)
                .onSuccess { response -> _uiState.update { it.copy(isLoading = false, designs = response.designs) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadDesigns()
    }

    fun toggleCategoryEditMode() {
        _uiState.update { it.copy(isCategoryEditMode = !it.isCategoryEditMode) }
    }

    /** 로컬 상태만 갱신한다 — 서버에 반영되는 카테고리 CRUD API가 없다. */
    fun addCategory(name: String) {
        if (name.isBlank()) return
        _uiState.update { state ->
            if (state.categories.any { it.equals(name, ignoreCase = true) }) state
            else state.copy(categories = state.categories + name)
        }
    }

    fun removeCategory(name: String) {
        if (name == "전체") return
        _uiState.update { state ->
            val nextCategories = state.categories - name
            val nextSelected = if (state.selectedCategory == name) "전체" else state.selectedCategory
            state.copy(categories = nextCategories, selectedCategory = nextSelected)
        }
    }

    fun toggleSelectMode() {
        _uiState.update { it.copy(isSelectMode = !it.isSelectMode, selectedDesignIds = emptySet()) }
    }

    fun toggleSelected(designId: Long) {
        _uiState.update { state ->
            val next = if (designId in state.selectedDesignIds) state.selectedDesignIds - designId else state.selectedDesignIds + designId
            state.copy(selectedDesignIds = next)
        }
    }

    fun deleteSelected() {
        val ids = _uiState.value.selectedDesignIds
        if (ids.isEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            var lastError: String? = null
            ids.forEach { id ->
                adminDesignRepository.deleteDesign(id).onFailure { e -> lastError = e.message }
            }
            _uiState.update {
                it.copy(isDeleting = false, isSelectMode = false, selectedDesignIds = emptySet(), errorMessage = lastError)
            }
            loadDesigns()
        }
    }
}
