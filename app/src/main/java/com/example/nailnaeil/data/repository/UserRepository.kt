package com.example.nailnaeil.data.repository

import com.example.nailnaeil.data.local.TokenStore
import com.example.nailnaeil.data.remote.UserApi
import com.example.nailnaeil.data.remote.apiCall
import com.example.nailnaeil.data.remote.apiCallUnit
import com.example.nailnaeil.data.remote.dto.UpdateUserRequest
import com.example.nailnaeil.data.remote.dto.UserAddress
import com.example.nailnaeil.data.remote.dto.UserAddressRequest
import com.example.nailnaeil.data.remote.dto.UserAddressUpdateRequest
import com.example.nailnaeil.data.remote.dto.UserMeResponse

class UserRepository(
    private val api: UserApi,
    private val tokenStore: TokenStore
) {

    suspend fun getMe(): Result<UserMeResponse> = apiCall { api.getMe() }

    suspend fun updateMe(email: String? = null, profileImageUrl: String? = null): Result<UserMeResponse> {
        return apiCall { api.updateMe(UpdateUserRequest(email = email, profileImageUrl = profileImageUrl)) }
    }

    suspend fun withdraw(): Result<Unit> {
        return apiCallUnit { api.withdraw() }.onSuccess { tokenStore.clear() }
    }

    suspend fun getAddresses(): Result<List<UserAddress>> {
        return apiCall { api.getAddresses() }.map { it.addresses.orEmpty() }
    }

    suspend fun addAddress(
        label: String,
        address: String,
        addressDetail: String?,
        latitude: Double,
        longitude: Double,
        isDefault: Boolean
    ): Result<UserAddress> {
        return apiCall { api.addAddress(UserAddressRequest(label, address, addressDetail, latitude, longitude, isDefault)) }
    }

    suspend fun updateAddress(addressId: Long, request: UserAddressUpdateRequest): Result<UserAddress> {
        return apiCall { api.updateAddress(addressId, request) }
    }

    suspend fun deleteAddress(addressId: Long): Result<Unit> {
        return apiCallUnit { api.deleteAddress(addressId) }
    }
}
