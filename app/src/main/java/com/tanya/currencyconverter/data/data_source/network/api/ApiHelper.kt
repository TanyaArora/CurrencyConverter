package com.tanya.currencyconverter.data.data_source.network.api

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
                        else -> {
                            var errorMessage = throwable.response()?.errorBody()?.string()
                            errorMessage = if (errorMessage.isNullOrEmpty())
                                "Http Error"
                            else errorMessage
                            ApiError.HttpError.GenericHttpError(
                                throwable.code(), errorMessage
                            )
                        }
                    }
                }

                else -> {
                    val errorMessage: String =
                        throwable.message?.let { if (it.isEmpty()) "Unknown Error" else throwable.message }
                            ?: "Unknown Error"
                    ApiError.Unknown(errorMessage)
                }
            }
        )
    }
}