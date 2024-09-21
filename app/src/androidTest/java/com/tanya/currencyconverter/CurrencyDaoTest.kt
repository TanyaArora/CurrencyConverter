package com.tanya.currencyconverter

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.tanya.currencyconverter.data.data_source.local.database.AppDatabase
import com.tanya.currencyconverter.data.data_source.local.database.CurrencyDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class CurrencyDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var currencyDao: CurrencyDao

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            appContext,
            AppDatabase::class.java
        ).build()
        currencyDao = database.currencyDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndGetCurrencies() = runTest {
        val currencies = getDummyLocalCurrenciesWithoutConversionRate()
        currencyDao.upsertAll(currencies)
        val actualResult = currencyDao.getCurrencies()
        assert(actualResult == currencies)
    }

    @Test
    fun testUpdateConversionRates() = runTest {
        val currencies = getDummyLocalCurrenciesWithoutConversionRate()
        currencyDao.upsertAll(currencies)

        val conversionRates = getDummyCurrencyRatesMap()
        currencyDao.updateConversionRates(conversionRates)

        val actualResult = currencyDao.getCurrencies()
        val expectedResult = getDummyLocalCurrenciesWithConversionRate()
        assert(actualResult == expectedResult)
    }
}