package com.example.paypayassignment.data.repository

import com.example.paypayassignment.data.data_source.datastore.PreferenceKeys
import com.example.paypayassignment.data.data_source.entity.CurrencyEntity
import com.example.paypayassignment.data.data_source.local.ILocalDataSource
import com.example.paypayassignment.data.data_source.local.LocalDataSource
import com.example.paypayassignment.data.data_source.network.INetworkDataSource
import com.example.paypayassignment.data.data_source.network.NetworkDataSource
import com.example.paypayassignment.data.data_source.network.api.ApiResponse
import com.example.paypayassignment.data.data_source.network.api.safeApiCall
import com.example.paypayassignment.data.mapper.toCurrencyList
import com.example.paypayassignment.data.utils.DataUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val localDataSource: ILocalDataSource,
    private val networkDataSource: INetworkDataSource,
    private val dataUtils: DataUtils,
) : IRepository {

    override suspend fun getCurrencies(): Flow<ApiResponse<List<CurrencyEntity>>> = flow {
        emit(ApiResponse.Loading)
        val networkUpdateRequired = dataUtils.updateCurrencies()
        val response = safeApiCall {
            if (networkUpdateRequired) {
                networkDataSource.getCurrencies().toCurrencyList().also {
                    localDataSource.saveCurrencies(it)
                }
                localDataSource.getCurrencies()
            } else localDataSource.getCurrencies()
        }
        if (response is ApiResponse.Success && networkUpdateRequired) {
            dataUtils.saveCurrentTime(PreferenceKeys.CURRENCIES_LAST_UPDATED_AT)
        }
        emit(response)
    }


    override suspend fun getConversionRates(): Flow<ApiResponse<List<CurrencyEntity>>> = flow {
        emit(ApiResponse.Loading)
        val networkUpdateRequired = dataUtils.updateConversionRates()
        val response = safeApiCall {
            if (networkUpdateRequired) {
                networkDataSource.getConversionRates().also {
                    localDataSource.saveConversionRate(it.rates)
                }
                localDataSource.getCurrencies()
            } else localDataSource.getCurrencies()
        }
        if (response is ApiResponse.Success && networkUpdateRequired) {
            dataUtils.saveCurrentTime(PreferenceKeys.CONVERSION_RATES_LAST_UPDATED_AT)
        }
        emit(response)
    }

}