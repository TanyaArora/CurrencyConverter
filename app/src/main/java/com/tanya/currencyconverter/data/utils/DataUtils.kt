package     com.tanya.currencyconverter.data.utils

import androidx.datastore.preferences.core.Preferences
import com.tanya.currencyconverter.data.data_source.datastore.AppPreferences
import com.tanya.currencyconverter.data.data_source.datastore.PreferenceKeys
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataUtils @Inject constructor(private val preferences: AppPreferences) {

    internal suspend fun updateCurrencies(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastFetchTime =
            preferences.getLongValue(PreferenceKeys.CURRENCIES_LAST_UPDATED_AT).first()
        return currentTime - lastFetchTime > 30 * 60 * 1000
    }

    internal suspend fun updateConversionRates(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastFetchTime =
            preferences.getLongValue(PreferenceKeys.CONVERSION_RATES_LAST_UPDATED_AT).first()
        return currentTime - lastFetchTime > 30 * 60 * 1000
    }

    internal suspend fun saveCurrentTime(key: Preferences.Key<Long>) {
        preferences.saveLongValue(
            key,
            System.currentTimeMillis()
        )
    }
}