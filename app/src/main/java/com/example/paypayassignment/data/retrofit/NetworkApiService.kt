package com.example.paypayassignment.data.retrofit

import com.example.paypayassignment.data.data_source.entity.ConversionRate
import retrofit2.http.GET

interface NetworkApiService {

    @GET("currencies.json")
    suspend fun getCurrencies(): Map<String, String>

    @GET("latest.json")
    suspend fun getConversionRates(): ConversionRate

}