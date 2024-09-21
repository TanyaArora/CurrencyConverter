package com.example.paypayassignment.data

import com.example.paypayassignment.data.data_source.local.LocalCurrency

//Convert getCurrencies result to Model
fun Map<String, String>.toCurrencyList(): List<Currency> {
    return this.map { Currency(it.key, it.value) }
}

fun List<LocalCurrency>.toCurrencyList(): List<Currency> {
    return this.map { Currency(it.code, it.name, it.usdConversionRate) }
}

fun List<Currency>.toLocalCurrencyList(): List<LocalCurrency> {
    return this.map { LocalCurrency(it.code, it.name, it.usdConversionRate) }
}