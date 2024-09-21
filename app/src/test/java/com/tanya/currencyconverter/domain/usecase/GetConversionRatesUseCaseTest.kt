package com.tanya.currencyconverter.domain.usecase

import app.cash.turbine.test
import com.tanya.currencyconverter.data.data_source.entity.CurrencyEntity
import com.tanya.currencyconverter.data.data_source.local.getDummyCurrencyEntities
import com.tanya.currencyconverter.data.data_source.network.api.ApiError
import com.tanya.currencyconverter.data.data_source.network.api.ApiResponse
import com.tanya.currencyconverter.data.repository.CurrencyRepository
import com.tanya.currencyconverter.domain.mapper.UiState
import com.tanya.currencyconverter.domain.model.Currency
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetConversionRatesUseCaseTest {

    @MockK
    lateinit var repository: CurrencyRepository

    private lateinit var getConversionRatesUseCase: GetConversionRatesUseCase

    private val baseAmount = 100.0
    private val baseCurrency = Currency(
        "USD", "United States Dollar", 1.0, null
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getConversionRatesUseCase = GetConversionRatesUseCase(repository)
    }

    // test loading state

    @Test
    fun `invoke should return UiState Loading when repository returns Loading`() = runTest {
        coEvery { repository.getConversionRates() } returns flow { emit(ApiResponse.Loading) }

        getConversionRatesUseCase.invoke(baseAmount, baseCurrency).test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            awaitComplete()
        }
    }

    // test success response

    @Test
    fun `use case should return UiState Success when repository returns Success`() = runTest {
        val currencies = getDummyCurrencyEntities()
        val expectedResult = listOf(
            Currency("USD", "United States Dollar", 1.0, 100.0),
            Currency("EUR", "Euro", 0.90, 90.0)
        )
        coEvery { repository.getConversionRates() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Success(currencies))
        }

        getConversionRatesUseCase(baseAmount, baseCurrency).test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(
                awaitItem(),
                UiState.Success(expectedResult)
            )
            awaitComplete()
        }
    }

    @Test
    fun `use case should return UiState Success with 0 convertedAmount when base amount is null`() =
        runTest {
            val currencies = getDummyCurrencyEntities()
            val expectedResult = listOf(
                Currency("USD", "United States Dollar", 1.0, null),
                Currency("EUR", "Euro", 0.90, null)
            )

            coEvery { repository.getConversionRates() } returns flow {
                emit(ApiResponse.Loading)
                emit(ApiResponse.Success(currencies))
            }

            getConversionRatesUseCase(null, baseCurrency).test {
                assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
                assertEquals(
                    awaitItem(),
                    UiState.Success(expectedResult)
                )
                awaitComplete()
            }
        }

    // test error responses

    @Test
    fun `use case should return UiState Error when repository returns Network error`() = runTest {
        coEvery { repository.getConversionRates() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Error(ApiError.HttpError.Network))
        }

        getConversionRatesUseCase(baseAmount, baseCurrency).test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Network Error"))
            awaitComplete()
        }
    }

    @Test
    fun `use case should return UiState Error when repository returns ServiceUnavailable error`() =
        runTest {
            coEvery { repository.getConversionRates() } returns flow {
                emit(ApiResponse.Loading)
                emit(ApiResponse.Error(ApiError.HttpError.ServiceUnavailable))
            }

            getConversionRatesUseCase(baseAmount, baseCurrency).test {
                assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
                assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Service Unavailable"))
                awaitComplete()
            }
        }

    @Test
    fun `use case should return UiState Error when repository returns Unauthorized error`() =
        runTest {
            coEvery { repository.getConversionRates() } returns flow {
                emit(ApiResponse.Loading)
                emit(ApiResponse.Error(ApiError.HttpError.Unauthorized))
            }

            getConversionRatesUseCase(baseAmount, baseCurrency).test {
                assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
                assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Unauthorized"))
                awaitComplete()
            }
        }

    @Test
    fun `use case should return UiState Error when repository returns NotFound error`() = runTest {
        coEvery { repository.getConversionRates() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Error(ApiError.HttpError.NotFound))
        }

        getConversionRatesUseCase(baseAmount, baseCurrency).test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Api Not found"))
            awaitComplete()
        }
    }

    @Test
    fun `use case should return UiState Error when repository returns GenericHttpError error`() =
        runTest {
            coEvery { repository.getConversionRates() } returns flow {
                emit(ApiResponse.Loading)
                emit(ApiResponse.Error(ApiError.HttpError.GenericHttpError(400, "Bad Request")))
            }

            getConversionRatesUseCase(baseAmount, baseCurrency).test {
                assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
                assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Bad Request"))
                awaitComplete()
            }
        }

    @Test
    fun `use case should return UiState Error when repository returns Unknown error`() = runTest {
        coEvery { repository.getConversionRates() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Error(ApiError.Unknown("Timeout exception")))
        }

        getConversionRatesUseCase(baseAmount, baseCurrency).test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Timeout exception"))
            awaitComplete()
        }
    }
}