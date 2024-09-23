package com.example.paypayassignment.data.mapper

import com.example.paypayassignment.data.data_source.entity.CurrencyEntity
import com.example.paypayassignment.data.data_source.local.LocalCurrency
import org.junit.Test

class EntityMapperTest {

    @Test
    fun `test currencies map conversion to currency entity list`() {
        val apiResponseMap =
            mapOf(
                "AED" to "United Arab Emirates Dirham",
                "AFN" to "Afghan Afghani"
            )
        val expectedResult = listOf(
            CurrencyEntity("AED", "United Arab Emirates Dirham"),
            CurrencyEntity("AFN", "Afghan Afghani")
        )
        val actualResult = apiResponseMap.toCurrencyList()
        assert(actualResult == expectedResult)
    }

    @Test
    fun `test local currencies map conversion to currency entity list`() {
        val localCurrencies = listOf(
            LocalCurrency("AED", "United Arab Emirates Dirham", 3.673),
            LocalCurrency("AFN", "Afghan Afghani", 68.858767)
        )
        val expectedResult = listOf(
            CurrencyEntity("AED", "United Arab Emirates Dirham", 3.673),
            CurrencyEntity("AFN", "Afghan Afghani", 68.858767)
        )
        val actualResult = localCurrencies.toCurrencyList()
        assert(actualResult == expectedResult)
    }

    @Test
    fun `test currency entity list conversion to local currency list`() {
        val currencyEntities = listOf(
            CurrencyEntity("AED", "United Arab Emirates Dirham", 3.673),
            CurrencyEntity("AFN", "Afghan Afghani", 68.858767)
        )

        val expectedResult = listOf(
            LocalCurrency("AED", "United Arab Emirates Dirham", 3.673),
            LocalCurrency("AFN", "Afghan Afghani", 68.858767)
        )

        val actualResult = currencyEntities.toLocalCurrencyList()
        assert(actualResult == expectedResult)
    }

}