package com.example.nailnaeil.ui.main.address

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.nailnaeil.data.remote.dto.UserAddress
import com.example.nailnaeil.di.AppContainer

/** 사용자 주소 목록(실 백엔드 연동)을 화면 간에 공유하기 위한 세션 상태. */
object AddressState {
    val addresses: SnapshotStateList<UserAddress> = mutableStateListOf()

    var selectedAddressId by mutableStateOf<Long?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /** 이번 세션에서 고른 주소가 있으면 그 주소, 없으면 기본 주소, 그것도 없으면 첫 번째 주소. */
    val selectedAddress: UserAddress?
        get() = addresses.find { it.addressId == selectedAddressId }
            ?: addresses.find { it.isDefault }
            ?: addresses.firstOrNull()

    fun currentAddressLabel(): String = selectedAddress?.label ?: "주소 설정"

    suspend fun refresh() {
        isLoading = true
        errorMessage = null
        AppContainer.userRepository.getAddresses()
            .onSuccess { list ->
                addresses.clear()
                addresses.addAll(list)
                if (selectedAddressId == null || addresses.none { it.addressId == selectedAddressId }) {
                    selectedAddressId = (addresses.find { it.isDefault } ?: addresses.firstOrNull())?.addressId
                }
            }
            .onFailure { e -> errorMessage = e.message }
        isLoading = false
    }

    fun setCurrent(addressId: Long) {
        selectedAddressId = addressId
    }

    fun findById(addressId: Long): UserAddress? = addresses.find { it.addressId == addressId }
}
