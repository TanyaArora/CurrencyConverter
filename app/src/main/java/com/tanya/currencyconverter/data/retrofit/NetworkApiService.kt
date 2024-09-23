package com.tanya.currencyconverter.data.retrofit

import com.tanya.currencyconverter.data.data_source.entity.ConversionRate
import retrofit2.http.GET

interface NetworkApiService {

    @GET("currencies.json")
    suspend fun getCurrencies(): Map<String, String>

    @GET("latest.json")
    suspend fun getConversionRates(): ConversionRate

}