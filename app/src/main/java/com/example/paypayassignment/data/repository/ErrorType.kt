package com.example.paypayassignment.data.repository

sealed class ErrorType {

    sealed class Api : ErrorType() {
        data object Network : Api()
        data object ServiceUnavailable : Api()
        data object NotFound : Api()
        data object Unauthorized : Api()
    }

    data class Unknown(val error: String) : ErrorType()
}