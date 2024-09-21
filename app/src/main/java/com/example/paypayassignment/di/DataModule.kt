package com.example.paypayassignment.di

import com.example.paypayassignment.data.data_source.network.INetworkDataSource
import com.example.paypayassignment.data.data_source.network.NetworkDataSource
import com.example.paypayassignment.data.repository.CurrencyRepository
import com.example.paypayassignment.data.repository.IRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindNetworkDataSource(
        networkDataSource: NetworkDataSource
    ): INetworkDataSource

}

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCurrencyRepository(
        currencyRepository: CurrencyRepository
    ): IRepository

}