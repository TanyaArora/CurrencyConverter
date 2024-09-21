package com.example.paypayassignment.data.repository

import com.example.paypayassignment.data.Currency

interface IRepository {

    suspend fun getCurrencies(): List<Currency>

    suspend fun getConversionRates(): List<Currency>
}