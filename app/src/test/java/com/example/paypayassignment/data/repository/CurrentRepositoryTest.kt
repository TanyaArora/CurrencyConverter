package com.example.paypayassignment.data.repository

import app.cash.turbine.test
import com.example.paypayassignment.data.data_source.datastore.PreferenceKeys
import com.example.paypayassignment.data.data_source.entity.CurrencyEntity
import com.example.paypayassignment.data.data_source.local.LocalDataSource
import com.example.paypayassignment.data.data_source.network.NetworkDataSource
import com.example.paypayassignment.data.data_source.network.api.ApiResponse
import com.example.paypayassignment.data.utils.DataUtils
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrentRepositoryTest {

    @MockK
    lateinit var localDataSource: LocalDataSource

    @MockK
    lateinit var networkDataSource: NetworkDataSource

    @MockK
    lateinit var dataUtils: DataUtils

    private lateinit var repository: CurrencyRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = CurrencyRepository(localDataSource, networkDataSource, dataUtils)
    }

    @Test
    fun `getCurrencies should return currencies from network when update is required`() = runTest {
        val currencies = listOf(CurrencyEntity("USD", "", 1.0))
        coEvery { dataUtils.updateCurrencies() } returns true
        coEvery { dataUtils.saveCurrentTime(any()) }
        coEvery { networkDataSource.getCurrencies() } returns currencies

        repository.getCurrencies().test {
            assertEquals(awaitItem(), ApiResponse.Loading)
            assertEquals(awaitItem(), ApiResponse.Success(currencies))
            awaitComplete()
        }

        coVerify(exactly = 1) { networkDataSource.getCurrencies() }
        coVerify(exactly = 1) { localDataSource.saveCurrencies(currencies) }
        coVerify(exactly = 1) { dataUtils.saveCurrentTime(PreferenceKeys.CURRENCIES_LAST_UPDATED_AT) }
    }
}