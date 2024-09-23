package com.tanya.currencyconverter.data.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class QueryParameterInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.newBuilder()
            .addQueryParameter("app_id", "6cf140b528de4c2383393b83596f85fd")
            .build()
        val request = originalRequest.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}