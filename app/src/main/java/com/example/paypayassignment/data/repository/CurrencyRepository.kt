package com.example.paypayassignment.data.repository

import com.example.paypayassignment.data.data_source.datastore.PreferenceKeys
import com.example.paypayassignment.data.data_source.entity.CurrencyEntity
import com.example.paypayassignment.data.data_source.local.LocalDataSource
import com.example.paypayassignment.data.data_source.network.NetworkDataSource
import com.example.paypayassignment.data.data_source.network.api.ApiResponse
import com.example.paypayassignment.data.data_source.network.api.safeApiCall
import com.example.paypayassignment.data.utils.DataUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val networkDataSource: NetworkDataSource,
    private val dataUtils: DataUtils,
) : IRepository {

    override suspend fun getCurrencies(): Flow<ApiResponse<List<CurrencyEntity>>> = flow {
        emit(ApiResponse.Loading)
        emit(safeApiCall {
            if (dataUtils.updateCurrencies()) {
                dataUtils.saveCurrentTime(PreferenceKeys.CURRENCIES_LAST_UPDATED_AT)
                networkDataSource.getCurrencies().also { localDataSource.saveCurrencies(it) }
            } else localDataSource.getCurrencies()
        })
    }


    override suspend fun getConversionRates(): Flow<ApiResponse<List<CurrencyEntity>>> = flow {
        emit(ApiResponse.Loading)
        emit(safeApiCall {
            if (dataUtils.updateConversionRates()) {
                dataUtils.saveCurrentTime(PreferenceKeys.CONVERSION_RATES_LAST_UPDATED_AT)
                networkDataSource.getConversionRates().also {
                    localDataSource.saveConversionRate(it.rates)
                }
                localDataSource.getCurrencies()
            } else localDataSource.getCurrencies()
        })
    }

}