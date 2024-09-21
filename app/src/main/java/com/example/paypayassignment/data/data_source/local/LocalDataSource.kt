package com.example.paypayassignment.data.data_source.local

import com.example.paypayassignment.data.Currency
import com.example.paypayassignment.data.data_source.local.database.CurrencyDao
import com.example.paypayassignment.data.toCurrencyList
import com.example.paypayassignment.data.toLocalCurrencyList
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val currencyDao: CurrencyDao) : ILocalDataSource {

    override suspend fun getCurrencies(): List<Currency> {
        return currencyDao.getCurrencies().toCurrencyList()
    }

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        currencyDao.upsertAll(currencies.toLocalCurrencyList())
    }

    override suspend fun saveConversionRate(conversionRates: Map<String, Double>) {
        currencyDao.updateConversionRates(conversionRates)
    }

}