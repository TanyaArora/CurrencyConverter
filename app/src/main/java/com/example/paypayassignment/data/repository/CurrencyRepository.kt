package com.example.paypayassignment.data.repository

import com.example.paypayassignment.data.data_source.datastore.AppPreferences
import com.example.paypayassignment.data.data_source.datastore.PreferenceKeys
import com.example.paypayassignment.data.data_source.local.LocalDataSource
import com.example.paypayassignment.data.data_source.network.NetworkDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val networkDataSource: NetworkDataSource,
    private val preferences: AppPreferences
) : IRepository {

    override suspend fun getCurrencies() = if (updateCurrencies()) {
        preferences.saveLongValue(
            PreferenceKeys.CURRENCIES_LAST_UPDATED_AT,
            System.currentTimeMillis()
        )
        networkDataSource.getCurrencies().also { localDataSource.saveCurrencies(it) }
    } else
        localDataSource.getCurrencies()

    override suspend fun getConversionRates() = if(updateConversionRates()){
        preferences.saveLongValue(PreferenceKeys.CONVERSION_RATES_LAST_UPDATED_AT, System.currentTimeMillis())
        val conversionRate = networkDataSource.getConversionRates()
        localDataSource.saveConversionRate(conversionRate.rates)
        localDataSource.getCurrencies()
    }else
        localDataSource.getCurrencies()

    private suspend fun updateCurrencies(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastFetchTime =
            preferences.getLongValue(PreferenceKeys.CURRENCIES_LAST_UPDATED_AT).first()
        return currentTime - lastFetchTime > 30 * 60 * 1000
    }

    private suspend fun updateConversionRates(): Boolean{
        val currentTime = System.currentTimeMillis()
        val lastFetchTime =
            preferences.getLongValue(PreferenceKeys.CONVERSION_RATES_LAST_UPDATED_AT).first()
        return currentTime - lastFetchTime > 30 * 60 * 1000
    }
}