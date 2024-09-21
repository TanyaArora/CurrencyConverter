package com.tanya.currencyconverter.domain.mapper

import com.tanya.currencyconverter.data.data_source.entity.CurrencyEntity
import com.tanya.currencyconverter.domain.model.Currency

fun List<CurrencyEntity>.toCurrency(
    baseAmount: Double? = null,
    baseCurrency: Currency? = null
): List<Currency> =
    this.map { currency ->
        Currency(
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
    baseCurrency: Currency?
): Double? {
    return if (baseAmount != null && baseCurrency != null) {
        if (baseCurrency.code == "USD")
            usdConversionRate?.times(baseAmount)
        else
            baseCurrency.usdConversionRate?.let { usdConversionRate?.times(baseAmount)?.div(it) }
    } else null
}