package com.example.paypayassignment.data.mapper

import com.example.paypayassignment.data.data_source.local.LocalCurrency
import com.example.paypayassignment.data.data_source.entity.CurrencyEntity

//Convert getCurrencies result to CurrencyEntity Model
fun Map<String, String>.toCurrencyList(): List<CurrencyEntity> {
    return this.map { CurrencyEntity(it.key, it.value) }
}

//Convert local currencies to CurrencyEntity Model used when data is fetched from db
fun List<LocalCurrency>.toCurrencyList(): List<CurrencyEntity> {
    return this.map { CurrencyEntity(it.code, it.name, it.usdConversionRate) }
}

//Convert CurrencyEntity Model to local currencies used to api response to local db
fun List<CurrencyEntity>.toLocalCurrencyList(): List<LocalCurrency> {
    return this.map { LocalCurrency(it.code, it.name, it.usdConversionRate) }
}