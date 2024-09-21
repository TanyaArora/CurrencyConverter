package com.example.paypayassignment.data.repository

import com.example.paypayassignment.data.data_source.entity.CurrencyEntity
import com.example.paypayassignment.data.data_source.network.api.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IRepository {

    suspend fun getCurrencies(): Flow<ApiResponse<List<CurrencyEntity>>>

    suspend fun getConversionRates(): Flow<ApiResponse<List<CurrencyEntity>>>
}