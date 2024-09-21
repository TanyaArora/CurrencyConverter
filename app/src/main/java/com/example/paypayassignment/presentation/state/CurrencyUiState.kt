package com.example.paypayassignment.presentation.state

import com.example.paypayassignment.domain.model.Currency

data class CurrencyUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val baseAmount: Double? = null,
    val baseCurrency: Currency = Currency(
        "USD",
        "United States Dollar",
        null,
        1.0
    ),
    val expanded: Boolean = false,
    val showConvertedCurrencies: Boolean = false,
    val enableButton: Boolean = false,
    val convertedCurrencies: List<Currency> = listOf()
)