package com.example.paypayassignment.domain.usecase

import com.example.paypayassignment.domain.mapper.mapToUiState
import com.example.paypayassignment.data.repository.CurrencyRepository
import com.example.paypayassignment.domain.mapper.UiState
import com.example.paypayassignment.domain.mapper.toCurrency
import com.example.paypayassignment.domain.model.Currency
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(private val repository: CurrencyRepository) {

    suspend operator fun invoke(): Flow<UiState<List<Currency>>> =
        repository.getCurrencies().mapToUiState { apiResponse ->
            apiResponse.toCurrency()
        }
}