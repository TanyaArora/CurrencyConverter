package com.tanya.currencyconverter

import com.tanya.currencyconverter.data.data_source.local.LocalCurrency

fun getDummyLocalCurrenciesWithoutConversionRate() = listOf(
    LocalCurrency("USD", "United States Dollar"),
    LocalCurrency("EUR", "Euro")
)

fun getDummyCurrencyRatesMap() = mapOf(
    "USD" to 1.0,
    "EUR" to 0.90
)

fun getDummyLocalCurrenciesWithConversionRate() = listOf(
    LocalCurrency("USD", "United States Dollar", 1.0),
    LocalCurrency("EUR", "Euro", 0.90)
)