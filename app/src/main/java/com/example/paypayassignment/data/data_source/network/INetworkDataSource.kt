package com.example.paypayassignment.data.data_source.network

import com.example.paypayassignment.data.data_source.entity.CurrencyEntity
import com.example.paypayassignment.data.data_source.entity.ConversionRate

interface INetworkDataSource {

    suspend fun getCurrencies(): List<CurrencyEntity>

    suspend fun getConversionRates(): ConversionRate

}