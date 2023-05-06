package com.example.kemonoreaderv2.utils

import com.example.kemonoreaderv2.Booleans

sealed class UIState {
    object LOADING: UIState()
    object LOADING2: UIState()
    data class SUCCESS (val success: List<String>? = null): UIState()
    data class SUCCESS2(val success2: Booleans?= null): UIState()
    data class ERROR   (val error: Exception): UIState()
}