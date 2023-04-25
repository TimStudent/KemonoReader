package com.example.kemonoreaderv2.utils

sealed class UIState {
    object LOADING: UIState()
    data class SUCCESS(
        val success: List<String>? = null
        /**
         * When success, show something
         */
    ): UIState()
    data class ERROR(val error: Exception): UIState()
}