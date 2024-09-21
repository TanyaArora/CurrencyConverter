package com.tanya.currencyconverter.data.data_source.entity

data class ConversionRate(
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)
