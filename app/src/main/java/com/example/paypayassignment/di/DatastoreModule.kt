package com.example.paypayassignment.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val CURRENCY_PREFERENCE_NAME = "currency_preferences.preferences_pb"

@Module
@InstallIn(SingletonComponent::class)
class DatastoreModule {

    @Singleton
    @Provides
    fun provideDatastore(@ApplicationContext context: Context) = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
        produceFile = {
            context.dataStoreFile(CURRENCY_PREFERENCE_NAME)
        }
    )
}