package com.tanya.currencyconverter.domain.mapper

import com.tanya.currencyconverter.data.data_source.network.api.ApiError
import com.tanya.currencyconverter.data.data_source.network.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/*
* T is the type of data Api returned
* R is the type of data we want to send to the UI
*/

fun <T, R : Any> Flow<ApiResponse<T>>.mapToUiState(transform: (T) -> R): Flow<UiState<R>> = map {
    when (it) {
        is ApiResponse.Error -> {
            when (it.error) {
                is ApiError.HttpError.Network -> UiState.Error("Network Error")
                is ApiError.HttpError.ServiceUnavailable -> UiState.Error("Service Unavailable")
                is ApiError.HttpError.Unauthorized -> UiState.Error("Unauthorized")
                is ApiError.HttpError.NotFound -> UiState.Error("Api Not found")
                is ApiError.HttpError.GenericHttpError -> UiState.Error(it.error.errorMessage)
                is ApiError.Unknown -> UiState.Error(it.error.errorMessage)
            }
        }

        is ApiResponse.Loading -> UiState.Loading()
        is ApiResponse.Success -> UiState.Success(transform(it.data))
    }
}.catch { throwable ->
    emit(UiState.Error(throwable.message ?: "Unknown Error"))
}