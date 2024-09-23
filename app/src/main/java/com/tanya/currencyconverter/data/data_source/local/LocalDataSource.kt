package com.tanya.currencyconverter.data.data_source.local

import com.tanya.currencyconverter.data.data_source.entity.CurrencyEntity
import com.tanya.currencyconverter.data.data_source.local.database.CurrencyDao
import com.tanya.currencyconverter.data.mapper.toCurrencyList
import com.tanya.currencyconverter.data.mapper.toLocalCurrencyList
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val currencyDao: CurrencyDao) : ILocalDataSource {

    override suspend fun getCurrencies(): List<CurrencyEntity> {
        return currencyDao.getCurrencies().toCurrencyList()
    }

    override suspend fun saveCurrencies(currencies: List<CurrencyEntity>) {
        currencyDao.upsertAll(currencies.toLocalCurrencyList())
    }

    override suspend fun saveConversionRate(conversionRates: Map<String, Double>) {
        currencyDao.updateConversionRates(conversionRates)
    }

}