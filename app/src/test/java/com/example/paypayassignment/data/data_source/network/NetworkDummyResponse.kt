package com.example.paypayassignment.data.data_source.network

import com.example.paypayassignment.data.data_source.entity.ConversionRate
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

fun getDummyCurrenciesFromNetwork() =
    mapOf("AED" to "United Arab Emirates Dirham", "AFN" to "Afghan Afghani")

fun getDummyConversionRates(): ConversionRate = ConversionRate(
    disclaimer = "Usage subject to terms: https://openexchangerates.org/terms",
    license = "https://openexchangerates.org/license",
    timestamp = 1726902020,
    base = "USD", rates = mapOf("AED" to 3.67296, "AFN" to 3.67296)
)

fun getDummyIoException() = IOException()

fun getDummyUnauthorizedError(): HttpException {
    val response = Response.error<Any>(401, "".toResponseBody(null))
    return HttpException(response)
}

fun getDummyApiNotFoundError(): HttpException {
    val response = Response.error<Any>(404, "".toResponseBody(null))
    return HttpException(response)
}

fun getDummyServiceUnavailableError(): HttpException {
    val response = Response.error<Any>(503, "".toResponseBody(null))
    return HttpException(response)
}

fun getDummyGenericHttpErrorWithNonEmptyError(): HttpException {
    val response = Response.error<Any>(400, "Bad Request".toResponseBody(null))
    return HttpException(response)
}

fun getDummyGenericHttpErrorWithEmptyError(): HttpException {
    val response = Response.error<Any>(400, "".toResponseBody(null))
    return HttpException(response)
}

fun getDummyUnknownErrorWithNonEmptyErrorMessage(): Throwable {
    return Throwable(message = "SocketTimeOutException")
}

fun getDummyUnknownErrorWithEmptyErrorMessage(): Throwable {
    return Throwable(message = "")
}
