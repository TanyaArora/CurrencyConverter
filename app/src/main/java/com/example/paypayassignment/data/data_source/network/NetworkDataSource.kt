package com.example.paypayassignment.data.data_source.network

import com.example.paypayassignment.data.Currency
import com.example.paypayassignment.data.data_source.model.ConversionRate
import com.example.paypayassignment.data.retrofit.NetworkApiService
import com.example.paypayassignment.data.toCurrencyList
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val apiService: NetworkApiService) :
    INetworkDataSource {

    override suspend fun getCurrencies(): List<Currency> =
        apiService.getCurrencies().toCurrencyList()

    override suspend fun getConversionRates(): ConversionRate = apiService.getConversionRates()

}