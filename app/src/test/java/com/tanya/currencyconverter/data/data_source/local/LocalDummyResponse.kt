package com.tanya.currencyconverter.data.data_source.local

import com.tanya.currencyconverter.data.data_source.entity.CurrencyEntity

fun getDummyCurrencyEntities() =
    listOf(
        CurrencyEntity("USD", "United States Dollar", 1.0),
        CurrencyEntity("EUR", "Euro", 0.90)
    )