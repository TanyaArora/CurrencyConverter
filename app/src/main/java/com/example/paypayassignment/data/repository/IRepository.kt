package com.example.paypayassignment.data.repository

import com.example.paypayassignment.data.data_source.entity.CurrencyEntity

interface IRepository {

    suspend fun getCurrencies(): List<CurrencyEntity>

    suspend fun getConversionRates(): List<CurrencyEntity>
}