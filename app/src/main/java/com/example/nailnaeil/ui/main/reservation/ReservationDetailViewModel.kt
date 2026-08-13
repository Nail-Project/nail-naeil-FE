package com.example.nailnaeil.ui.main.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.ReservationDetailResponse
import com.example.nailnaeil.data.repository.ReservationRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReservationDetailUiState(
    val isLoading: Boolean = true,
    val reservation: ReservationDetailResponse? = null,
    val errorMessage: String? = null,
    val isCancelling: Boolean = false,
    val cancelError: String? = null,
    val cancelled: Boolean = false
)

class ReservationDetailViewModel(
    private val reservationId: Long,
    private val repository: ReservationRepository = AppContainer.reservationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservationDetailUiState())
    val uiState: StateFlow<ReservationDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getReservationDetail(reservationId)
                .onSuccess { reservation -> _uiState.update { it.copy(isLoading = false, reservation = reservation) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun cancel(reason: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCancelling = true, cancelError = null) }
            repository.cancelReservation(reservationId, reason)
                .onSuccess { _uiState.update { it.copy(isCancelling = false, cancelled = true) } }
                .onFailure { e -> _uiState.update { it.copy(isCancelling = false, cancelError = e.message) } }
        }
    }
}
