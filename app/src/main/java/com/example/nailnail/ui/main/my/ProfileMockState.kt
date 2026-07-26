package com.example.nailnail.ui.main.my

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ProfileMockState {
    var nickname by mutableStateOf("네일내일")
    var name by mutableStateOf("")
    var phone by mutableStateOf("")
    var email by mutableStateOf("nailtomorrow@gmail.com")
}
