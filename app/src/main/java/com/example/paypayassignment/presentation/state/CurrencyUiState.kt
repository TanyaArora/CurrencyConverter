package com.example.paypayassignment.presentation.state

data class CurrencyUiState(
    val isLoading: Boolean = false,
    val baseAmount: Double? = null,
    val baseCurrency: CurrencyItemUiState? = CurrencyItemUiState(
        "USD",
        "United States Dollar",
        null,
        1.0
    ),
    val expanded: Boolean = false,
    val showConvertedCurrencies: Boolean = false,
    val convertedCurrencies: List<CurrencyItemUiState> = listOf()
)