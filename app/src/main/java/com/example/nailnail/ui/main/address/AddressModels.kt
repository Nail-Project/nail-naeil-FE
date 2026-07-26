package com.example.nailnail.ui.main.address

enum class AddressIconType { HOME, WORK, OTHER }

data class Address(
    val id: String,
    val label: String,
    val roadAddress: String,
    val detailAddress: String = "",
    val icon: AddressIconType = AddressIconType.OTHER
)
