package com.tanya.currencyconverter.data.data_source.datastore

import androidx.datastore.preferences.core.longPreferencesKey

// data store keys file
object PreferenceKeys {
    val CURRENCIES_LAST_UPDATED_AT = longPreferencesKey("currencies_last_updated_at")
    val CONVERSION_RATES_LAST_UPDATED_AT = longPreferencesKey("conversion_rates_last_updated_at")
}