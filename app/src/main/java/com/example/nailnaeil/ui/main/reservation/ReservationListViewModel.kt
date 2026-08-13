package com.example.nailnaeil.ui.main.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.ReservationListItem
import com.example.nailnaeil.data.remote.dto.ReservationQueryStatus
import com.example.nailnaeil.data.repository.ReservationRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReservationListUiState(
    val isLoadingConfirmed: Boolean = true,
    val isLoadingPast: Boolean = true,
    val confirmed: List<ReservationListItem> = emptyList(),
    val past: List<ReservationListItem> = emptyList(),
    val confirmedSortAscending: Boolean = true,
    val pastSortAscending: Boolean = true,
    val errorMessage: String? = null
)

class ReservationListViewModel(
    private val repository: ReservationRepository = AppContainer.reservationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservationListUiState())
    val uiState: StateFlow<ReservationListUiState> = _uiState.asStateFlow()

    init {
        loadConfirmed()
        loadPast()
    }

    fun loadConfirmed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingConfirmed = true) }
            repository.getReservations(ReservationQueryStatus.CONFIRMED, size = 50)
                .onSuccess { response -> _uiState.update { it.copy(isLoadingConfirmed = false, confirmed = response.reservations) } }
                .onFailure { e -> _uiState.update { it.copy(isLoadingConfirmed = false, errorMessage = e.message) } }
        }
    }

    fun loadPast() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPast = true) }
            repository.getReservations(ReservationQueryStatus.PAST, size = 50)
                .onSuccess { response -> _uiState.update { it.copy(isLoadingPast = false, past = response.reservations) } }
                .onFailure { e -> _uiState.update { it.copy(isLoadingPast = false, errorMessage = e.message) } }
        }
    }

    fun toggleConfirmedSort() {
        _uiState.update { it.copy(confirmedSortAscending = !it.confirmedSortAscending) }
    }

    fun togglePastSort() {
        _uiState.update { it.copy(pastSortAscending = !it.pastSortAscending) }
    }
}
