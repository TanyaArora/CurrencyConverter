package com.tanya.currencyconverter.data.data_source.network

import com.tanya.currencyconverter.data.data_source.entity.ConversionRate
import com.tanya.currencyconverter.data.retrofit.NetworkApiService
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val apiService: NetworkApiService) :
    INetworkDataSource {

    override suspend fun getCurrencies(): Map<String, String> = apiService.getCurrencies()

    override suspend fun getConversionRates(): ConversionRate = apiService.getConversionRates()

}