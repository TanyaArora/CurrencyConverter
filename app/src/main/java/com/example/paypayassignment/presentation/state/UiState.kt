package com.example.paypayassignment.presentation.state

sealed class UiState<T>{
    class Loading<T>: UiState<T>()
    data class Success<T>(val data: Any): UiState<T>()
    data class Error<T>(val message: String): UiState<T>()
}