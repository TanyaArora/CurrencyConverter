package com.example.paypayassignment.data.repository

sealed class Result<out T>{
    data class Success<T>(val data: T): Result<T>()
    data class Error<T>(val errorType: ErrorType): Result<T>()
}