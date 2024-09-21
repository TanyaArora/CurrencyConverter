package com.example.paypayassignment.data.data_source.local

import com.example.paypayassignment.data.Currency

interface ILocalDataSource {

    suspend fun getCurrencies(): List<Currency>

    suspend fun saveCurrencies(currencies: List<Currency>)

    suspend fun saveConversionRate(conversionRates: Map<String, Double>)
}