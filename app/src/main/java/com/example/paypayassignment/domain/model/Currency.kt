package com.example.paypayassignment.domain.model

data class Currency(
    val code: String,
    val name: String,
    val usdConversionRate: Double?,
    val convertedAmount: Double?
)
