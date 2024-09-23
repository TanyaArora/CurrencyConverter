package com.tanya.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.tanya.currencyconverter.data.data_source.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppDatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "currency_database"
    ).build()

    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase) = appDatabase.currencyDao()
}