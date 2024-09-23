package com.example.paypayassignment.data.repository

import app.cash.turbine.test
import com.example.paypayassignment.data.data_source.entity.CurrencyEntity
import com.example.paypayassignment.data.data_source.local.LocalDataSource
import com.example.paypayassignment.data.data_source.network.NetworkDataSource
import com.example.paypayassignment.data.data_source.network.api.ApiError
import com.example.paypayassignment.data.data_source.network.api.ApiResponse
import com.example.paypayassignment.data.data_source.local.getDummyCurrencyEntities
import com.example.paypayassignment.data.mapper.toCurrencyList
import com.example.paypayassignment.data.data_source.network.getDummyApiNotFoundError
import com.example.paypayassignment.data.data_source.network.getDummyConversionRates
import com.example.paypayassignment.data.data_source.network.getDummyCurrenciesFromNetwork
import com.example.paypayassignment.data.data_source.network.getDummyGenericHttpErrorWithEmptyError
import com.example.paypayassignment.data.data_source.network.getDummyGenericHttpErrorWithNonEmptyError
import com.example.paypayassignment.data.data_source.network.getDummyIoException
import com.example.paypayassignment.data.data_source.network.getDummyServiceUnavailableError
import com.example.paypayassignment.data.data_source.network.getDummyUnauthorizedError
import com.example.paypayassignment.data.data_source.network.getDummyUnknownErrorWithEmptyErrorMessage
import com.example.paypayassignment.data.data_source.network.getDummyUnknownErrorWithNonEmptyErrorMessage
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

    //getCurrencies positive cases

    @Test
    fun `getCurrencies should return currencies from network when update is required`() = runTest {
        val currencies = getDummyCurrenciesFromNetwork()
        val currencyList = currencies.toCurrencyList()
        coEvery { dataUtils.updateCurrencies() } returns true
        coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
        coEvery { networkDataSource.getCurrencies() } returns currencies
        coEvery { localDataSource.saveCurrencies(any()) } returns Unit
        coEvery { localDataSource.getCurrencies() } returns currencyList

        repository.getCurrencies().test {
            assertEquals(awaitItem(), ApiResponse.Loading)
            assertEquals(awaitItem(), ApiResponse.Success(currencyList))
            awaitComplete()
        }

        coVerify(exactly = 1) { networkDataSource.getCurrencies() }
        coVerify(exactly = 1) { localDataSource.saveCurrencies(any()) }
        coVerify(exactly = 1) { dataUtils.saveCurrentTime(any()) }
    }

    @Test
    fun `getCurrencies should return currencies from local data source when update is not required`() =
        runTest {
            val currencies = getDummyCurrencyEntities()
            coEvery { dataUtils.updateCurrencies() } returns false
            coEvery { localDataSource.getCurrencies() } returns currencies

            repository.getCurrencies().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(awaitItem(), ApiResponse.Success(currencies))
                awaitComplete()
            }

            coVerify(exactly = 1) { localDataSource.getCurrencies() }
            coVerify(exactly = 0) { networkDataSource.getCurrencies() }
            coVerify(exactly = 0) { localDataSource.saveCurrencies(currencies) }
            coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
        }

    //getCurrencies negative cases

    @Test
    fun `getCurrencies should return network error when throws IOException`() = runTest {
        val apiResponse = getDummyIoException()

        coEvery { dataUtils.updateCurrencies() } returns true
        coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
        coEvery { networkDataSource.getCurrencies() } throws apiResponse

        repository.getCurrencies().test {
            assertEquals(awaitItem(), ApiResponse.Loading)
            assertEquals(awaitItem(), ApiResponse.Error<CurrencyEntity>(ApiError.HttpError.Network))
            awaitComplete()
        }

        coVerify(exactly = 1) { networkDataSource.getCurrencies() }
        coVerify(exactly = 0) { localDataSource.saveCurrencies(any()) }
        coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
        coVerify(exactly = 0) { localDataSource.getCurrencies() }
    }

    @Test
    fun `getCurrencies should return unauthorized error when api returns 401`() = runTest {
        val apiResponse = getDummyUnauthorizedError()

        coEvery { dataUtils.updateCurrencies() } returns true
        coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
        coEvery { networkDataSource.getCurrencies() } throws apiResponse

        repository.getCurrencies().test {
            assertEquals(awaitItem(), ApiResponse.Loading)
            assertEquals(
                awaitItem(),
                ApiResponse.Error<CurrencyEntity>(ApiError.HttpError.Unauthorized)
            )
            awaitComplete()
        }

        coVerify(exactly = 1) { networkDataSource.getCurrencies() }
        coVerify(exactly = 0) { localDataSource.saveCurrencies(any()) }
        coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
        coVerify(exactly = 0) { localDataSource.getCurrencies() }
    }

    @Test
    fun `getCurrencies should return not found error when api returns 404`() = runTest {
        val apiResponse = getDummyApiNotFoundError()

        coEvery { dataUtils.updateCurrencies() } returns true
        coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
        coEvery { networkDataSource.getCurrencies() } throws apiResponse

        repository.getCurrencies().test {
            assertEquals(awaitItem(), ApiResponse.Loading)
            assertEquals(
                awaitItem(),
                ApiResponse.Error<CurrencyEntity>(ApiError.HttpError.NotFound)
            )
            awaitComplete()
        }

        coVerify(exactly = 1) { networkDataSource.getCurrencies() }
        coVerify(exactly = 0) { localDataSource.saveCurrencies(any()) }
        coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
        coVerify(exactly = 0) { localDataSource.getCurrencies() }
    }

    @Test
    fun `getCurrencies should return service unavailable error when api returns 503`() = runTest {
        val apiResponse = getDummyServiceUnavailableError()

        coEvery { dataUtils.updateCurrencies() } returns true
        coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
        coEvery { networkDataSource.getCurrencies() } throws apiResponse

        repository.getCurrencies().test {
            assertEquals(awaitItem(), ApiResponse.Loading)
            assertEquals(
                awaitItem(),
                ApiResponse.Error<CurrencyEntity>(ApiError.HttpError.ServiceUnavailable)
            )
            awaitComplete()
        }

        coVerify(exactly = 1) { networkDataSource.getCurrencies() }
        coVerify(exactly = 0) { localDataSource.saveCurrencies(any()) }
        coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
        coVerify(exactly = 0) { localDataSource.getCurrencies() }
    }

    @Test
    fun `getCurrencies should return message from non nullable error body when api returns a non-handled Http error`() =
        runTest {
            val apiResponse = getDummyGenericHttpErrorWithNonEmptyError()

            coEvery { dataUtils.updateCurrencies() } returns true
            coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
            coEvery { networkDataSource.getCurrencies() } throws apiResponse

            repository.getCurrencies().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(
                    awaitItem(),
                    ApiResponse.Error<CurrencyEntity>(
                        ApiError.HttpError.GenericHttpError(
                            400,
                            "Bad Request"
                        )
                    )
                )
                awaitComplete()
            }

            coVerify(exactly = 1) { networkDataSource.getCurrencies() }
            coVerify(exactly = 0) { localDataSource.saveCurrencies(any()) }
            coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
            coVerify(exactly = 0) { localDataSource.getCurrencies() }
        }

    @Test
    fun `getCurrencies should return Http Error when api returns a non-handled Http error with empty error body`() =
        runTest {
            val apiResponse = getDummyGenericHttpErrorWithEmptyError()

            coEvery { dataUtils.updateCurrencies() } returns true
            coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
            coEvery { networkDataSource.getCurrencies() } throws apiResponse

            repository.getCurrencies().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(
                    awaitItem(),
                    ApiResponse.Error<CurrencyEntity>(
                        ApiError.HttpError.GenericHttpError(
                            400,
                            "Http Error"
                        )
                    )
                )
                awaitComplete()
            }

            coVerify(exactly = 1) { networkDataSource.getCurrencies() }
            coVerify(exactly = 0) { localDataSource.saveCurrencies(any()) }
            coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
            coVerify(exactly = 0) { localDataSource.getCurrencies() }
        }

    @Test
    fun `getCurrencies should return error message when api returns a non-handled Exception with non-empty error message`() =
        runTest {
            val apiResponse = getDummyUnknownErrorWithNonEmptyErrorMessage()

            coEvery { dataUtils.updateCurrencies() } returns true
            coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
            coEvery { networkDataSource.getCurrencies() } throws apiResponse

            repository.getCurrencies().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(
                    awaitItem(),
                    ApiResponse.Error<CurrencyEntity>(
                        ApiError.Unknown("SocketTimeOutException")
                    )
                )
                awaitComplete()
            }

            coVerify(exactly = 1) { networkDataSource.getCurrencies() }
            coVerify(exactly = 0) { localDataSource.saveCurrencies(any()) }
            coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
            coVerify(exactly = 0) { localDataSource.getCurrencies() }
        }

    @Test
    fun `getCurrencies should return Unknown error when api returns a non-handled Exception with empty error message`() =
        runTest {
            val apiResponse = getDummyUnknownErrorWithEmptyErrorMessage()

            coEvery { dataUtils.updateCurrencies() } returns true
            coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
            coEvery { networkDataSource.getCurrencies() } throws apiResponse

            repository.getCurrencies().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(
                    awaitItem(),
                    ApiResponse.Error<CurrencyEntity>(
                        ApiError.Unknown("Unknown Error")
                    )
                )
                awaitComplete()
            }

            coVerify(exactly = 1) { networkDataSource.getCurrencies() }
            coVerify(exactly = 0) { localDataSource.saveCurrencies(any()) }
            coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
            coVerify(exactly = 0) { localDataSource.getCurrencies() }
        }

    //getConversionRates positive cases

    @Test
    fun `getConversionRates should return conversion rates from network when update is required`() =
        runTest {
            val conversionRate = getDummyConversionRates()
            val localCurrencies = getDummyCurrencyEntities()

            coEvery { dataUtils.updateConversionRates() } returns true
            coEvery { networkDataSource.getConversionRates() } returns conversionRate
            coEvery { localDataSource.saveConversionRate(any()) } returns Unit
            coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
            coEvery { localDataSource.getCurrencies() } returns localCurrencies

            repository.getConversionRates().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(awaitItem(), ApiResponse.Success(localCurrencies))
                awaitComplete()
            }

            coVerify(exactly = 1) { networkDataSource.getConversionRates() }
            coVerify(exactly = 1) { dataUtils.saveCurrentTime(any()) }
            coVerify(exactly = 1) { localDataSource.getCurrencies() }
        }

    @Test
    fun `getConversionRates should return conversion from local db when update is not required`() =
        runTest {
            val currencies = getDummyCurrencyEntities()

            coEvery { dataUtils.updateConversionRates() } returns false
            coEvery { localDataSource.getCurrencies() } returns currencies

            repository.getConversionRates().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(awaitItem(), ApiResponse.Success(currencies))
                awaitComplete()
            }

            coVerify(exactly = 1) { localDataSource.getCurrencies() }
            coVerify(exactly = 0) { networkDataSource.getConversionRates() }
            coVerify(exactly = 0) { localDataSource.saveConversionRate(any()) }
            coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
        }

    //getConversionRates negative cases

    @Test
    fun `getConversionRates should return network error when throws IOException`() = runTest {
        val apiResponse = getDummyIoException()

        coEvery { dataUtils.updateConversionRates() } returns true
        coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
        coEvery { networkDataSource.getConversionRates() } throws apiResponse

        repository.getConversionRates().test {
            assertEquals(awaitItem(), ApiResponse.Loading)
            assertEquals(awaitItem(), ApiResponse.Error<CurrencyEntity>(ApiError.HttpError.Network))
            awaitComplete()
        }

        coVerify(exactly = 1) { networkDataSource.getConversionRates() }
        coVerify(exactly = 0) { localDataSource.saveCurrencies(any()) }
        coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
        coVerify(exactly = 0) { localDataSource.getCurrencies() }
    }

    @Test
    fun `getConversionRates should return unauthorized error when api returns 401`() = runTest {
        val apiResponse = getDummyUnauthorizedError()

        coEvery { dataUtils.updateConversionRates() } returns true
        coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
        coEvery { networkDataSource.getConversionRates() } throws apiResponse

        repository.getConversionRates().test {
            assertEquals(awaitItem(), ApiResponse.Loading)
            assertEquals(
                awaitItem(),
                ApiResponse.Error<CurrencyEntity>(ApiError.HttpError.Unauthorized)
            )
            awaitComplete()
        }

        coVerify(exactly = 1) { networkDataSource.getConversionRates() }
        coVerify(exactly = 0) { localDataSource.saveConversionRate(any()) }
        coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
        coVerify(exactly = 0) { localDataSource.getCurrencies() }
    }

    @Test
    fun `getConversionRates should return not found error when api returns 404`() = runTest {
        val apiResponse = getDummyApiNotFoundError()

        coEvery { dataUtils.updateConversionRates() } returns true
        coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
        coEvery { networkDataSource.getConversionRates() } throws apiResponse

        repository.getConversionRates().test {
            assertEquals(awaitItem(), ApiResponse.Loading)
            assertEquals(
                awaitItem(),
                ApiResponse.Error<CurrencyEntity>(ApiError.HttpError.NotFound)
            )
            awaitComplete()
        }

        coVerify(exactly = 1) { networkDataSource.getConversionRates() }
        coVerify(exactly = 0) { localDataSource.saveConversionRate(any()) }
        coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
        coVerify(exactly = 0) { localDataSource.getCurrencies() }
    }

    @Test
    fun `getConversionRates should return service unavailable error when api returns 503`() = runTest {
        val apiResponse = getDummyServiceUnavailableError()

        coEvery { dataUtils.updateConversionRates() } returns true
        coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
        coEvery { networkDataSource.getConversionRates() } throws apiResponse

        repository.getConversionRates().test {
            assertEquals(awaitItem(), ApiResponse.Loading)
            assertEquals(
                awaitItem(),
                ApiResponse.Error<CurrencyEntity>(ApiError.HttpError.ServiceUnavailable)
            )
            awaitComplete()
        }

        coVerify(exactly = 1) { networkDataSource.getConversionRates() }
        coVerify(exactly = 0) { localDataSource.saveConversionRate(any()) }
        coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
        coVerify(exactly = 0) { localDataSource.getCurrencies() }
    }

    @Test
    fun `getConversionRates should return message from non nullable error body when api returns a non-handled Http error`() =
        runTest {
            val apiResponse = getDummyGenericHttpErrorWithNonEmptyError()

            coEvery { dataUtils.updateConversionRates() } returns true
            coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
            coEvery { networkDataSource.getConversionRates() } throws apiResponse

            repository.getConversionRates().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(
                    awaitItem(),
                    ApiResponse.Error<CurrencyEntity>(
                        ApiError.HttpError.GenericHttpError(
                            400,
                            "Bad Request"
                        )
                    )
                )
                awaitComplete()
            }

            coVerify(exactly = 1) { networkDataSource.getConversionRates() }
            coVerify(exactly = 0) { localDataSource.saveConversionRate(any()) }
            coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
            coVerify(exactly = 0) { localDataSource.getCurrencies() }
        }

    @Test
    fun `getConversionRates should return Http Error when api returns a non-handled Http error with empty error body`() =
        runTest {
            val apiResponse = getDummyGenericHttpErrorWithEmptyError()

            coEvery { dataUtils.updateConversionRates() } returns true
            coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
            coEvery { networkDataSource.getConversionRates() } throws apiResponse

            repository.getConversionRates().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(
                    awaitItem(),
                    ApiResponse.Error<CurrencyEntity>(
                        ApiError.HttpError.GenericHttpError(
                            400,
                            "Http Error"
                        )
                    )
                )
                awaitComplete()
            }

            coVerify(exactly = 1) { networkDataSource.getConversionRates() }
            coVerify(exactly = 0) { localDataSource.saveConversionRate(any()) }
            coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
            coVerify(exactly = 0) { localDataSource.getCurrencies() }
        }

    @Test
    fun `getConversionRates should return error message when api returns a non-handled Exception with non-empty error message`() =
        runTest {
            val apiResponse = getDummyUnknownErrorWithNonEmptyErrorMessage()

            coEvery { dataUtils.updateConversionRates() } returns true
            coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
            coEvery { networkDataSource.getConversionRates() } throws apiResponse

            repository.getConversionRates().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(
                    awaitItem(),
                    ApiResponse.Error<CurrencyEntity>(
                        ApiError.Unknown("SocketTimeOutException")
                    )
                )
                awaitComplete()
            }

            coVerify(exactly = 1) { networkDataSource.getConversionRates() }
            coVerify(exactly = 0) { localDataSource.saveConversionRate(any()) }
            coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
            coVerify(exactly = 0) { localDataSource.getCurrencies() }
        }

    @Test
    fun `getConversionRates should return Unknown error when api returns a non-handled Exception with empty error message`() =
        runTest {
            val apiResponse = getDummyUnknownErrorWithEmptyErrorMessage()

            coEvery { dataUtils.updateConversionRates() } returns true
            coEvery { dataUtils.saveCurrentTime(any()) } returns Unit
            coEvery { networkDataSource.getConversionRates() } throws apiResponse

            repository.getConversionRates().test {
                assertEquals(awaitItem(), ApiResponse.Loading)
                assertEquals(
                    awaitItem(),
                    ApiResponse.Error<CurrencyEntity>(
                        ApiError.Unknown("Unknown Error")
                    )
                )
                awaitComplete()
            }

            coVerify(exactly = 1) { networkDataSource.getConversionRates() }
            coVerify(exactly = 0) { localDataSource.saveConversionRate(any()) }
            coVerify(exactly = 0) { dataUtils.saveCurrentTime(any()) }
            coVerify(exactly = 0) { localDataSource.getCurrencies() }
        }

}