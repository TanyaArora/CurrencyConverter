package com.example.paypayassignment.data.data_source.network.api

sealed class ApiError {

    sealed class HttpError : ApiError() {
        data object Network : HttpError()
        data object ServiceUnavailable : HttpError()
        data object NotFound : HttpError()
        data object Unauthorized : HttpError()
        data class GenericHttpError(val code: Int, val errorMessage: String) : HttpError()
    }

    data class Unknown(val errorMessage: String) : ApiError()
}