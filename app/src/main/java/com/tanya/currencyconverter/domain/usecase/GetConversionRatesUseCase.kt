package com.tanya.currencyconverter.domain.usecase

import com.tanya.currencyconverter.data.repository.CurrencyRepository
import com.tanya.currencyconverter.domain.mapper.mapToUiState
import com.tanya.currencyconverter.domain.mapper.toCurrency
import com.tanya.currencyconverter.domain.model.Currency
import javax.inject.Inject

class GetConversionRatesUseCase @Inject constructor(private val repository: CurrencyRepository) {

    suspend operator fun invoke(baseAmount: Double?, baseCurrency: Currency) =
        repository.getConversionRates().mapToUiState { apiResponse ->
            apiResponse.toCurrency(baseAmount, baseCurrency)
        }
}