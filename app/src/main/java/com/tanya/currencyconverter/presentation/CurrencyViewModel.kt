package com.tanya.currencyconverter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.currencyconverter.domain.mapper.UiState
import com.tanya.currencyconverter.domain.usecase.GetConversionRatesUseCase
import com.tanya.currencyconverter.domain.usecase.GetCurrenciesUseCase
import com.tanya.currencyconverter.domain.model.Currency
import com.tanya.currencyconverter.presentation.state.CurrencyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getConversionRatesUseCase: GetConversionRatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        getCurrencies()
    }

    fun getCurrencies() {
        viewModelScope.launch {
            getCurrenciesUseCase.invoke().flowOn(Dispatchers.IO).collect { useCaseState ->
                when (useCaseState) {
                    is UiState.Loading ->
                        _uiState.update {
                            it.copy(isLoading = true)
                        }

                    is UiState.Success ->
                        _uiState.update {
                            it.copy(convertedCurrencies = useCaseState.data, isLoading = false)
                        }

                    is UiState.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = useCaseState.errorMessage, isLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun convertCurrency() {
        viewModelScope.launch {
            val baseAmount = _uiState.value.baseAmount
            val baseCurrency = _uiState.value.baseCurrency
            getConversionRatesUseCase.invoke(baseAmount, baseCurrency).flowOn(Dispatchers.IO).collect { useCaseState ->
                when (useCaseState) {
                    is UiState.Loading ->
                        _uiState.update {
                            it.copy(isLoading = true)
                        }

                    is UiState.Success ->
                        _uiState.update {
                            it.copy(
                                convertedCurrencies = useCaseState.data,
                                isLoading = false,
                                showConvertedCurrencies = true
                            )
                        }

                    is UiState.Error -> {
                        _uiState.update {
                            it.copy(errorMessage = useCaseState.errorMessage, isLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun updateBaseAmount(amountText: String) {
        val amount = amountText.toDoubleOrNull()
        _uiState.update {
            it.copy(
                baseAmount = amount,
                showConvertedCurrencies = if (amount == null || amount == 0.0) false else it.showConvertedCurrencies,
                enableButton = amount != null && amount != 0.0
            )
        }
    }

    fun onCurrencySelected(currency: Currency) {
        _uiState.update {
            it.copy(baseCurrency = currency)
        }
    }

    fun updateDropdownExpandState(expanded: Boolean? = null) {
        _uiState.update {
            it.copy(expanded = expanded ?: !it.expanded)
        }
    }
}