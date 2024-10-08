package com.tanya.currencyconverter.data.data_source.local

import com.tanya.currencyconverter.data.data_source.entity.CurrencyEntity

interface ILocalDataSource {

    suspend fun getCurrencies(): List<CurrencyEntity>

    suspend fun saveCurrencies(currencies: List<CurrencyEntity>)

    suspend fun saveConversionRate(conversionRates: Map<String, Double>)
}