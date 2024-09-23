package com.tanya.currencyconverter.data.data_source.network

import com.tanya.currencyconverter.data.data_source.entity.ConversionRate

interface INetworkDataSource {

    suspend fun getCurrencies(): Map<String, String>

    suspend fun getConversionRates(): ConversionRate

}