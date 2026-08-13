package com.example.nailnaeil.ui.main.estimate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nailnaeil.data.remote.dto.EstimateProposalDetailResponse
import com.example.nailnaeil.data.remote.dto.EstimateProposalTime
import com.example.nailnaeil.data.remote.dto.ShopReviewItem
import com.example.nailnaeil.data.repository.EstimateRepository
import com.example.nailnaeil.data.repository.ReservationRepository
import com.example.nailnaeil.data.repository.ReviewRepository
import com.example.nailnaeil.data.repository.ShopRepository
import com.example.nailnaeil.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ShopDetailUiState(
    val isLoading: Boolean = true,
    val proposal: EstimateProposalDetailResponse? = null,
    val thumbnailImageUrl: String? = null,
    val proposalTimes: List<EstimateProposalTime> = emptyList(),
    val recentReviews: List<ShopReviewItem> = emptyList(),
    val selectedTimeId: Long? = null,
    val errorMessage: String? = null,
    val isReserving: Boolean = false,
    val reserveError: String? = null,
    val reservationConfirmed: Boolean = false
)

class ShopDetailViewModel(
    private val proposalId: Long,
    private val estimateRepository: EstimateRepository = AppContainer.estimateRepository,
    private val shopRepository: ShopRepository = AppContainer.shopRepository,
    private val reviewRepository: ReviewRepository = AppContainer.reviewRepository,
    private val reservationRepository: ReservationRepository = AppContainer.reservationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShopDetailUiState())
    val uiState: StateFlow<ShopDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val detailResult = estimateRepository.getEstimateProposalDetail(proposalId)
            val timesResult = estimateRepository.getEstimateProposalTimes(proposalId)

            val proposal = detailResult.getOrNull()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    proposal = proposal,
                    proposalTimes = timesResult.getOrNull()?.proposalTimes ?: emptyList(),
                    errorMessage = detailResult.exceptionOrNull()?.message
                )
            }

            if (proposal != null) {
                shopRepository.getShopDetail(proposal.shop.id).onSuccess { shopDetail ->
                    _uiState.update { it.copy(thumbnailImageUrl = shopDetail.thumbnailImageUrl) }
                }
                reviewRepository.getShopReviews(proposal.shop.id, size = 5).onSuccess { reviews ->
                    _uiState.update { it.copy(recentReviews = reviews.reviews) }
                }
            }
        }
    }

    fun selectTime(timeId: Long) {
        _uiState.update { it.copy(selectedTimeId = timeId) }
    }

    fun reserve() {
        val timeId = _uiState.value.selectedTimeId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isReserving = true, reserveError = null) }
            reservationRepository.createReservation(proposalId, timeId)
                .onSuccess { _uiState.update { it.copy(isReserving = false, reservationConfirmed = true) } }
                .onFailure { e -> _uiState.update { it.copy(isReserving = false, reserveError = e.message) } }
        }
    }
}
