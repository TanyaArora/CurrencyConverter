package com.example.paypayassignment.data.mapper

import com.example.paypayassignment.data.data_source.local.LocalCurrency
import com.example.paypayassignment.data.data_source.entity.CurrencyEntity

//Convert getCurrencies result to Model
fun Map<String, String>.toCurrencyList(): List<CurrencyEntity> {
    return this.map { CurrencyEntity(it.key, it.value) }
}

fun List<LocalCurrency>.toCurrencyList(): List<CurrencyEntity> {
    return this.map { CurrencyEntity(it.code, it.name, it.usdConversionRate) }
}

fun List<CurrencyEntity>.toLocalCurrencyList(): List<LocalCurrency> {
    return this.map { LocalCurrency(it.code, it.name, it.usdConversionRate) }
}