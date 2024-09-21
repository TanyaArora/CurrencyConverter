package com.example.paypayassignment.data.repository

import java.io.IOException
import java.net.SocketTimeoutException

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