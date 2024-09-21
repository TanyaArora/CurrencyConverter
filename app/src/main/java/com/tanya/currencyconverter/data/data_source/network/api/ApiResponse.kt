package com.tanya.currencyconverter.data.data_source.network.api

sealed class ApiResponse<out T> {
    data object Loading : ApiResponse<Nothing>()
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error<T>(val error: ApiError) : ApiResponse<T>()
}