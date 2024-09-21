package com.example.paypayassignment.data.data_source.network.api

import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResponse<T> {
    return try {
        ApiResponse.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        ApiResponse.Error(
            when (throwable) {
                is IOException -> ApiError.HttpError.Network
                is HttpException -> {
                    when (throwable.code()) {
                        401 -> ApiError.HttpError.Unauthorized
                        404 -> ApiError.HttpError.NotFound
                        503 -> ApiError.HttpError.ServiceUnavailable
                        else -> ApiError.HttpError.GenericHttpError(
                            throwable.code(),
                            throwable.message ?: "Unknown Error"
                        )
                    }
                }
                else -> ApiError.Unknown(throwable.message ?: "Unknown Error")
            }
        )
    }
}