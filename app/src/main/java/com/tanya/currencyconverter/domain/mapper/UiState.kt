package com.tanya.currencyconverter.domain.mapper

sealed class UiState<T> {
    data class Loading<T>(val value: T? = null) : UiState<T>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error<T>(val errorMessage: String) : UiState<T>()
}