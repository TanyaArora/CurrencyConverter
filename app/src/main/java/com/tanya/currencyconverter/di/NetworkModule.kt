package com.tanya.currencyconverter.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tanya.currencyconverter.R
import com.tanya.currencyconverter.data.retrofit.NetworkApiService
import com.tanya.currencyconverter.data.retrofit.QueryParameterInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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

    @Provides
    @Singleton
    fun providesBaseUrl(@ApplicationContext context: Context) = context.getString(R.string.base_url)

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(baseUrl: String, okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()

    @Singleton
    @Provides
    fun provideNetworkService(retrofit: Retrofit): NetworkApiService =
        retrofit.create(NetworkApiService::class.java)

}