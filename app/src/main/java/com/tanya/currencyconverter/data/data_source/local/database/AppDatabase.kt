package com.tanya.currencyconverter.data.data_source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tanya.currencyconverter.data.data_source.local.LocalCurrency

@Database(entities = [LocalCurrency::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
}