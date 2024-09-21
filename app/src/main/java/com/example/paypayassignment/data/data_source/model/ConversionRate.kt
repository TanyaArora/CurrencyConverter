package com.example.paypayassignment.data.data_source.model

data class ConversionRate(
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)
