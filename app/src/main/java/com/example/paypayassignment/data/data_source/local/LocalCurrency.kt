package com.example.paypayassignment.data.data_source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class LocalCurrency(
    @PrimaryKey val code: String,
    val name: String,
    val usdConversionRate: Double? = null
)
