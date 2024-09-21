package com.example.paypayassignment.data.data_source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveLongValue(key: Preferences.Key<Long>, value: Long) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun getLongValue(key: Preferences.Key<Long>) = dataStore.data.map { preferences ->
        preferences[key] ?: 0L
    }

}
