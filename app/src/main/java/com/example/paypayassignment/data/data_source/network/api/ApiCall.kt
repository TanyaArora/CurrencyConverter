package com.example.paypayassignment.data.data_source.network.api

import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        Result.Error(
            when (throwable) {
                is IOException -> ErrorType.Api.Network
//                is SocketTimeoutException -> ErrorType.Api.ServiceUnavailable
                else -> ErrorType.Unknown(throwable.message ?: "Unknown Error")
            }
        )
    }
}