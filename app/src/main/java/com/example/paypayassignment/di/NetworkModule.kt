package com.example.paypayassignment.di

import com.example.paypayassignment.data.retrofit.NetworkApiService
import com.example.paypayassignment.data.retrofit.QueryParameterInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun providesQueryParameterInterceptor(): Interceptor = QueryParameterInterceptor()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        queryParameterInterceptor: QueryParameterInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .addInterceptor(queryParameterInterceptor).build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl("https://openexchangerates.org/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

    @Singleton
    @Provides
    fun provideNetworkService(retrofit: Retrofit): NetworkApiService =
        retrofit.create(NetworkApiService::class.java)

}