package com.example.paypayassignment.presentation

import android.util.Log
import com.example.paypayassignment.data.Currency
import com.example.paypayassignment.presentation.state.CurrencyItemUiState

fun List<Currency>.toCurrencyItemUiState(
    baseAmount: Double? = null,
    baseCurrency: CurrencyItemUiState? = null
): List<CurrencyItemUiState> =
    this.map { currency ->
        CurrencyItemUiState(
            code = currency.code,
            name = currency.name,
            usdConversionRate = currency.usdConversionRate,
            convertedAmount = getConvertedAmount(
                currency.usdConversionRate,
                baseAmount,
                baseCurrency
            )
        )
    }

//usdConversionRate is the conversion rate of USD to destination currency
fun getConvertedAmount(
    usdConversionRate: Double?,
    baseAmount: Double?,
    baseCurrency: CurrencyItemUiState?
): Double? {
//    Log.d("Tanya", "baseAmount: $baseAmount, usdConversionRate: $usdConversionRate, baseCurrency: $baseCurrency")
    return if (baseAmount != null && baseCurrency != null) {
        if (baseCurrency.code == "USD")
            usdConversionRate?.times(baseAmount)
        else
            baseCurrency.usdConversionRate?.let { usdConversionRate?.times(baseAmount)?.div(it) }
    } else null
}