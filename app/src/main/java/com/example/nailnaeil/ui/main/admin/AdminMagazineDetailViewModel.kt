package com.example.nailnaeil.ui.main.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.DesignAdminCreateRequest
import com.example.nailnaeil.data.remote.dto.DesignAdminUpdateRequest
import com.example.nailnaeil.data.remote.dto.DesignDetailResponse
import com.example.nailnaeil.data.repository.AdminDesignRepository
import com.example.nailnaeil.data.repository.DesignRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminMagazineDetailUiState(
    val isLoading: Boolean = false,
    val existingDesign: DesignDetailResponse? = null,
    val errorMessage: String? = null,
    val isSaving: Boolean = false,
    val saveError: String? = null,
    val saveSuccess: Boolean = false
)

class AdminMagazineDetailViewModel(
    private val designId: Long?,
    private val designRepository: DesignRepository = AppContainer.designRepository,
    private val adminDesignRepository: AdminDesignRepository = AppContainer.adminDesignRepository
) : ViewModel() {

    val isCreateMode: Boolean = designId == null

    private val _uiState = MutableStateFlow(AdminMagazineDetailUiState(isLoading = designId != null))
    val uiState: StateFlow<AdminMagazineDetailUiState> = _uiState.asStateFlow()

    init {
        if (designId != null) loadDesign(designId)
    }

    private fun loadDesign(id: Long) {
        viewModelScope.launch {
            designRepository.getDesignDetail(id)
                .onSuccess { design -> _uiState.update { it.copy(isLoading = false, existingDesign = design) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun create(request: DesignAdminCreateRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveError = null) }
            adminDesignRepository.createDesign(request)
                .onSuccess { _uiState.update { it.copy(isSaving = false, saveSuccess = true) } }
                .onFailure { e -> _uiState.update { it.copy(isSaving = false, saveError = e.message) } }
        }
    }

    fun update(request: DesignAdminUpdateRequest) {
        val id = designId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveError = null) }
            adminDesignRepository.updateDesign(id, request)
                .onSuccess { _uiState.update { it.copy(isSaving = false, saveSuccess = true) } }
                .onFailure { e -> _uiState.update { it.copy(isSaving = false, saveError = e.message) } }
        }
    }
}
