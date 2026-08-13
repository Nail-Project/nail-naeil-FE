package com.example.nailnaeil.data.remote.dto

/** 최초 가입 시 입력한 이름(nickname)과 전화번호는 수정할 수 없어 제외한다. */
data class UpdateUserRequest(
    val email: String? = null,
    val profileImageUrl: String? = null
)
