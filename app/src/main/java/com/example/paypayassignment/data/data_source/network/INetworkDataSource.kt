package com.example.paypayassignment.data.data_source.network

import com.example.paypayassignment.data.data_source.entity.ConversionRate

interface INetworkDataSource {

    suspend fun getCurrencies(): Map<String, String>

    suspend fun getConversionRates(): ConversionRate

}