package com.example.lastfmtest.data.retrofit

import com.example.lastfmtest.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class LastFmApiInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain
            .request()
            .url()
            .newBuilder()
            .addQueryParameter("api_key", BuildConfig.LAST_FM_API_KEY)
            .addQueryParameter("format", "json")
            .build()

        return chain.proceed(
            chain.request().newBuilder()
                .url(url)
                .build()
        )
    }
}