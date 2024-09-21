package com.example.paypayassignment.domain.mapper

import com.example.paypayassignment.data.data_source.network.api.ApiError
import com.example.paypayassignment.data.data_source.network.api.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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
                else -> UiState.Error("Unknown Error")
            }
        }

        is ApiResponse.Loading -> UiState.Loading()
        is ApiResponse.Success -> UiState.Success(transform(it.data))
    }
}.catch { throwable ->
    emit(UiState.Error(throwable.message ?: "Unknown Error"))
}

fun <T> Flow<UiState<T>>.handleResponse(
    scope: CoroutineScope,
    onSuccess: (T) -> Unit,
    onLoading: () -> Unit,
    onError: (String) -> Unit
) {
    scope.launch {
        collect {
            when (it) {
                is UiState.Error -> {

                }

                is UiState.Loading -> {

                }

                is UiState.Success -> {
                    onSuccess(it.data)
                }
            }
        }
    }

}