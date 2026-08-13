package com.example.nailnaeil.ui.main.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.UserMeResponse
import com.example.nailnaeil.data.repository.UserRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserMeResponse? = null,
    val errorMessage: String? = null,
    val isSaving: Boolean = false,
    val saveError: String? = null,
    val saveSuccess: Boolean = false
)

/** 마이페이지/내 정보/프로필 수정 화면이 공유하는 뷰모델. 화면마다 개별 인스턴스로 생성되어 각자 getMe()를 호출한다. */
class ProfileViewModel(
    private val userRepository: UserRepository = AppContainer.userRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            userRepository.getMe()
                .onSuccess { profile -> _uiState.update { it.copy(isLoading = false, profile = profile) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    /** 서버가 수정 가능하다고 정의한 필드는 email/profileImageUrl뿐이다(닉네임·전화번호는 가입 시 고정). */
    fun updateEmail(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveError = null, saveSuccess = false) }
            userRepository.updateMe(email = email)
                .onSuccess { profile -> _uiState.update { it.copy(isSaving = false, profile = profile, saveSuccess = true) } }
                .onFailure { e -> _uiState.update { it.copy(isSaving = false, saveError = e.message) } }
        }
    }

    fun consumeSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}
