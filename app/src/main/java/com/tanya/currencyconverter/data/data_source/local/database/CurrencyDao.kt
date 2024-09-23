package com.tanya.currencyconverter.data.data_source.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.tanya.currencyconverter.data.data_source.local.LocalCurrency

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies")
    suspend fun getCurrencies(): List<LocalCurrency>

    @Upsert
    suspend fun upsertAll(list: List<LocalCurrency>)

    @Transaction
    suspend fun updateConversionRates(currencyRates: Map<String, Double>) {
        currencyRates.forEach { currencyRate ->
            updateConversionRate(currencyRate.key, currencyRate.value)
        }
    }

    @Query("UPDATE currencies SET usdConversionRate = :conversionRate WHERE code = :code")
    suspend fun updateConversionRate(code: String, conversionRate: Double)
}