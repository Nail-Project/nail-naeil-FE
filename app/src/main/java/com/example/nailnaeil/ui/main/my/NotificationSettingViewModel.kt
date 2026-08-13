package com.example.nailnaeil.ui.main.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.repository.NotificationRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotificationSettingUiState(
    val isLoading: Boolean = true,
    val estimateEnabled: Boolean = true,
    val reservationEnabled: Boolean = true,
    val marketingEnabled: Boolean = false,
    val errorMessage: String? = null
)

class NotificationSettingViewModel(
    private val notificationRepository: NotificationRepository = AppContainer.notificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationSettingUiState())
    val uiState: StateFlow<NotificationSettingUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    fun loadSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            notificationRepository.getNotificationSettings()
                .onSuccess { settings ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            estimateEnabled = settings.estimateEnabled,
                            reservationEnabled = settings.reservationEnabled,
                            marketingEnabled = settings.marketingEnabled
                        )
                    }
                }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun setReservationEnabled(enabled: Boolean) {
        _uiState.update { it.copy(reservationEnabled = enabled) }
        viewModelScope.launch { notificationRepository.updateNotificationSettings(reservationEnabled = enabled) }
    }

    fun setEstimateEnabled(enabled: Boolean) {
        _uiState.update { it.copy(estimateEnabled = enabled) }
        viewModelScope.launch { notificationRepository.updateNotificationSettings(estimateEnabled = enabled) }
    }

    fun setMarketingEnabled(enabled: Boolean) {
        _uiState.update { it.copy(marketingEnabled = enabled) }
        viewModelScope.launch { notificationRepository.updateNotificationSettings(marketingEnabled = enabled) }
    }
}
