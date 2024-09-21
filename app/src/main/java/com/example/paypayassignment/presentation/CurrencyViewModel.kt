package com.example.paypayassignment.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paypayassignment.domain.GetConversionRatesUseCase
import com.example.paypayassignment.domain.GetCurrenciesUseCase
import com.example.paypayassignment.presentation.state.CurrencyItemUiState
import com.example.paypayassignment.presentation.state.CurrencyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getConversionRatesUseCase: GetConversionRatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        getCurrencies()
    }

    private fun getCurrencies() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val result = async(Dispatchers.IO) { getCurrenciesUseCase.invoke() }.await()
                Log.d("Tanya", "getCurrencies: $result")
                withContext(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(
                            convertedCurrencies = result.toCurrencyItemUiState(), isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("Tanya", "getCurrencies: $e")
                withContext(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(isLoading = false)
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
                showConvertedCurrencies = if (amount == null || amount == 0.0) false else it.showConvertedCurrencies
            )
        }
    }

    fun onCurrencySelected(currency: CurrencyItemUiState) {
        _uiState.update {
            it.copy(baseCurrency = currency)
        }
    }

    fun updateDropdownExpandState(expanded: Boolean? = null) {
        _uiState.update {
            it.copy(expanded = expanded ?: !it.expanded)
        }
    }

    fun convertCurrency() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = async { getConversionRatesUseCase.invoke() }.await()
                Log.d("Tanya", "convertCurrency: $result")
                Log.d("Tanya", "baseAmount: ${_uiState.value.baseAmount}")
                val baseAmount = _uiState.value.baseAmount
                val baseCurrency = _uiState.value.baseCurrency

                val convertedCurrencies = result.toCurrencyItemUiState(
                    baseAmount, baseCurrency
                )

                withContext(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(
                            convertedCurrencies = convertedCurrencies,
                            isLoading = false,
                            showConvertedCurrencies = true
                        )
                    }
                    Log.d("Tanya", "converted currencies ${_uiState.value.convertedCurrencies}")
                }
            } catch (e: Exception) {
                Log.d("Tanya", "convertCurrency: $e")
            }
        }
    }
}