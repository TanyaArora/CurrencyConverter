package com.example.paypayassignment.domain.mapper

import com.example.paypayassignment.domain.model.Currency
import org.junit.Test

//Testing the currency conversion functionality
class UiStateMapperTest {

    @Test
    fun `getConvertedAmount should return null if baseAmount is null`() {
        val actualResult = getConvertedAmount(
            usdConversionRate = 3.673,
            baseAmount = null,
            baseCurrency = Currency("USD", "", 1.0, 1.0)
        )
        val expectedResult = null
        assert(actualResult == expectedResult)
    }

    @Test
    fun `getConvertedAmount should return 0 if baseAmount is 0`() {
        val actualResult = getConvertedAmount(
            usdConversionRate = 3.673,
            baseAmount = 0.0,
            baseCurrency = Currency("USD", "", 1.0, 1.0)
        )
        val expectedResult = 0.0
        assert(actualResult == expectedResult)
    }

    @Test
    fun `getConvertedAmount should return null if baseCurrency is null`() {
        val actualResult = getConvertedAmount(
            usdConversionRate = 3.673,
            baseAmount = 100.0,
            baseCurrency = null
        )
        val expectedResult = null
        assert(actualResult == expectedResult)
    }

    //Converting 60USD to AED
    @Test
    fun `getConvertedAmount should return converted amount for USD base currency`() {
        val actualResult = getConvertedAmount(
            usdConversionRate = 3.673,
            baseAmount = 60.0,
            baseCurrency = Currency("USD", "", 1.0, 1.0)
        )
        val expectedResult = 220.38
        assert(actualResult == expectedResult)
    }

    //Converting 60AED to INR
    //Checking only the integer part and ignoring the decimal part
    @Test
    fun `getConvertedAmount should return converted amount for non USD base currency`() {
        val actualResult = getConvertedAmount(
            usdConversionRate = 83.774858,
            baseAmount = 60.0,
            baseCurrency = Currency("AED", "", 3.673, 3.673)
        )
        val expectedResult = 1368.49
        assert(actualResult?.toInt() == expectedResult.toInt())
    }
}