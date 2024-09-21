package com.tanya.currencyconverter.domain.usecase

import com.tanya.currencyconverter.domain.mapper.mapToUiState
import com.tanya.currencyconverter.data.repository.CurrencyRepository
import com.tanya.currencyconverter.domain.mapper.UiState
import com.tanya.currencyconverter.domain.mapper.toCurrency
import com.tanya.currencyconverter.domain.model.Currency
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(private val repository: CurrencyRepository) {

    suspend operator fun invoke(): Flow<UiState<List<Currency>>> =
        repository.getCurrencies().mapToUiState { apiResponse ->
            apiResponse.toCurrency()
        }
}