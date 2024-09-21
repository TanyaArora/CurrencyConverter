package com.example.paypayassignment.presentation.state

data class CurrencyItemUiState(
    val code: String,
    val name: String,
    val usdConversionRate: Double?,
    val convertedAmount: Double?
)
