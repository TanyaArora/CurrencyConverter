package com.example.paypayassignment.data.data_source.network

import com.example.paypayassignment.data.data_source.entity.CurrencyEntity
import com.example.paypayassignment.data.data_source.entity.ConversionRate
import com.example.paypayassignment.data.retrofit.NetworkApiService
import com.example.paypayassignment.data.mapper.toCurrencyList
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val apiService: NetworkApiService) :
    INetworkDataSource {

    override suspend fun getCurrencies(): List<CurrencyEntity> =
        apiService.getCurrencies().toCurrencyList()

    override suspend fun getConversionRates(): ConversionRate = apiService.getConversionRates()

}