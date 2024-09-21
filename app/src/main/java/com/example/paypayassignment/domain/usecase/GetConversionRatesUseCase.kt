package com.example.paypayassignment.domain.usecase

import com.example.paypayassignment.data.repository.CurrencyRepository
import javax.inject.Inject

class GetConversionRatesUseCase @Inject constructor(private val repository: CurrencyRepository) {

    suspend operator fun invoke() = repository.getConversionRates()
}