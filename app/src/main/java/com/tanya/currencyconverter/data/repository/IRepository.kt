package com.tanya.currencyconverter.data.repository

import com.tanya.currencyconverter.data.data_source.entity.CurrencyEntity
import com.tanya.currencyconverter.data.data_source.network.api.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IRepository {

    suspend fun getCurrencies(): Flow<ApiResponse<List<CurrencyEntity>>>

    suspend fun getConversionRates(): Flow<ApiResponse<List<CurrencyEntity>>>
}