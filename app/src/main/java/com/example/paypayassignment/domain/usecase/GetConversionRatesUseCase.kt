package com.example.paypayassignment.domain.usecase

import com.example.paypayassignment.data.repository.CurrencyRepository
import com.example.paypayassignment.domain.mapper.mapToUiState
import com.example.paypayassignment.domain.mapper.toCurrency
import com.example.paypayassignment.domain.model.Currency
import javax.inject.Inject

class GetConversionRatesUseCase @Inject constructor(private val repository: CurrencyRepository) {

    suspend operator fun invoke(baseAmount: Double?, baseCurrency: Currency) =
        repository.getConversionRates().mapToUiState { apiResponse ->
            apiResponse.toCurrency(baseAmount, baseCurrency)
        }
}