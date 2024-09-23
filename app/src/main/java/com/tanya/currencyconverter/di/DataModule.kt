package com.tanya.currencyconverter.di

import com.tanya.currencyconverter.data.data_source.local.ILocalDataSource
import com.tanya.currencyconverter.data.data_source.local.LocalDataSource
import com.tanya.currencyconverter.data.data_source.network.INetworkDataSource
import com.tanya.currencyconverter.data.data_source.network.NetworkDataSource
import com.tanya.currencyconverter.data.repository.CurrencyRepository
import com.tanya.currencyconverter.data.repository.IRepository
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

    @Binds
    abstract fun bindLocalDataSource(
        localDataSource: LocalDataSource
    ): ILocalDataSource
}

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCurrencyRepository(
        currencyRepository: CurrencyRepository
    ): IRepository

}