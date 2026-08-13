package com.example.nailnaeil.data.remote.dto

data class UserAddress(
    val addressId: Long,
    val label: String,
    val address: String,
    val addressDetail: String?,
    val latitude: Double,
    val longitude: Double,
    val isDefault: Boolean
)

data class UserAddressRequest(
    val label: String,
    val address: String,
    val addressDetail: String?,
    val latitude: Double,
    val longitude: Double,
    val isDefault: Boolean
)

/** 수정할 필드만 전달한다. 하나 이상의 필드가 필요하다. */
data class UserAddressUpdateRequest(
    val label: String? = null,
    val address: String? = null,
    val addressDetail: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isDefault: Boolean? = null
)

data class UserAddressListResponse(
    val addresses: List<UserAddress>? = emptyList()
)
