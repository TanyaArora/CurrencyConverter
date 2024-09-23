package com.example.paypayassignment.domain.usecase

import app.cash.turbine.test
import com.example.paypayassignment.data.data_source.entity.CurrencyEntity
import com.example.paypayassignment.data.data_source.local.getDummyCurrencyEntities
import com.example.paypayassignment.data.data_source.network.api.ApiError
import com.example.paypayassignment.data.data_source.network.api.ApiResponse
import com.example.paypayassignment.data.repository.CurrencyRepository
import com.example.paypayassignment.domain.mapper.UiState
import com.example.paypayassignment.domain.mapper.toCurrency
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCurrenciesUseCaseTest {

    @MockK
    lateinit var repository: CurrencyRepository

    private lateinit var getCurrenciesUseCase: GetCurrenciesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getCurrenciesUseCase = GetCurrenciesUseCase(repository)
    }

    // test loading state

    @Test
    fun `invoke should return UiState Loading when repository returns Loading`() = runTest {
        coEvery { repository.getCurrencies() } returns flow { emit(ApiResponse.Loading) }

        getCurrenciesUseCase().test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            awaitComplete()
        }
    }

    // test success response

    @Test
    fun `use case should return UiState Success when repository returns Success`() = runTest {
        val currencies = getDummyCurrencyEntities()
        coEvery { repository.getCurrencies() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Success(currencies))
        }

        getCurrenciesUseCase().test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(awaitItem(), UiState.Success(currencies.toCurrency()))
            awaitComplete()
        }
    }

    //test error responses

    @Test
    fun `use case should return UiState Error when repository returns Network error`() = runTest {
        coEvery { repository.getCurrencies() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Error(ApiError.HttpError.Network))
        }

        getCurrenciesUseCase().test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Network Error"))
            awaitComplete()
        }
    }

    @Test
    fun `use case should return UiState Error when repository returns ServiceUnavailable error`() = runTest {
        coEvery { repository.getCurrencies() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Error(ApiError.HttpError.ServiceUnavailable))
        }

        getCurrenciesUseCase().test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Service Unavailable"))
            awaitComplete()
        }
    }

    @Test
    fun `use case should return UiState Error when repository returns Unauthorized error`() = runTest {
        coEvery { repository.getCurrencies() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Error(ApiError.HttpError.Unauthorized))
        }

        getCurrenciesUseCase().test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Unauthorized"))
            awaitComplete()
        }
    }

    @Test
    fun `use case should return UiState Error when repository returns NotFound error`() = runTest {
        coEvery { repository.getCurrencies() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Error(ApiError.HttpError.NotFound))
        }

        getCurrenciesUseCase().test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Api Not found"))
            awaitComplete()
        }
    }

    @Test
    fun `use case should return UiState Error when repository returns GenericHttpError error`() = runTest {
        coEvery { repository.getCurrencies() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Error(ApiError.HttpError.GenericHttpError(400, "Bad Request")))
        }

        getCurrenciesUseCase().test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Bad Request"))
            awaitComplete()
        }
    }

    @Test
    fun `use case should return UiState Error when repository returns Unknown error`() = runTest {
        coEvery { repository.getCurrencies() } returns flow {
            emit(ApiResponse.Loading)
            emit(ApiResponse.Error(ApiError.Unknown("Timeout exception")))
        }

        getCurrenciesUseCase().test {
            assertEquals(awaitItem(), UiState.Loading<CurrencyEntity>())
            assertEquals(awaitItem(), UiState.Error<CurrencyEntity>("Timeout exception"))
            awaitComplete()
        }
    }
}