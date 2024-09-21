package com.example.paypayassignment.data.data_source.network

import com.example.paypayassignment.data.Currency
import com.example.paypayassignment.data.data_source.model.ConversionRate

interface INetworkDataSource {

    suspend fun getCurrencies(): List<Currency>

    suspend fun getConversionRates(): ConversionRate

}