package com.example.paypayassignment.data.data_source.network

import com.example.paypayassignment.data.data_source.entity.ConversionRate
import com.example.paypayassignment.data.retrofit.NetworkApiService
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val apiService: NetworkApiService) :
    INetworkDataSource {

    override suspend fun getCurrencies(): Map<String, String> = apiService.getCurrencies()

    override suspend fun getConversionRates(): ConversionRate = apiService.getConversionRates()

}