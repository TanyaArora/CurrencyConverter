package com.tanya.currencyconverter.data.data_source.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConversionRate(
    @Json
    val disclaimer: String,
    @Json
    val license: String,
    @Json
    val timestamp: Long,
    @Json
    val base: String,
    @Json
    val rates: Map<String, Double>
)
