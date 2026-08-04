package com.example.nailnaeil.ui.main.address

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** 백엔드 연동 전까지 주소 목록을 화면 간에 공유하기 위한 mock 인메모리 상태. */
object AddressMockState {
    val addresses = mutableStateListOf(
        Address("home", "우리집", "서울 동작구 상도동", icon = AddressIconType.HOME),
        Address("work", "회사", "서울 동작구 상도동", icon = AddressIconType.WORK),
        Address("addr1", "경기 안산시 상록구 한양대학로", "경기 안산시 상록구 한양대학로 55 한양대학교"),
        Address("addr2", "잠실종합운동장", "서울 송파구 올림픽로 25"),
        Address("addr3", "뚝섬한강공원", "서울 광진구 강변북로 2202"),
        Address("addr4", "경기 용인시 수지구 수지로 487", "경기 용인시 수지구 수지로 487"),
        Address("addr5", "제주 제주시", "제주 제주시 백포북길 25"),
    )

    var currentAddressId by mutableStateOf("home")
        private set

    fun currentAddressLabel(): String = addresses.find { it.id == currentAddressId }?.label ?: "주소 설정"

    fun setCurrent(id: String) {
        currentAddressId = id
    }

    fun delete(id: String) {
        addresses.removeAll { it.id == id }
        if (currentAddressId == id) {
            currentAddressId = addresses.firstOrNull()?.id.orEmpty()
        }
    }

    fun findById(id: String): Address? = addresses.find { it.id == id }

    fun upsert(address: Address) {
        val index = addresses.indexOfFirst { it.id == address.id }
        if (index >= 0) addresses[index] = address else addresses.add(address)
    }
}
