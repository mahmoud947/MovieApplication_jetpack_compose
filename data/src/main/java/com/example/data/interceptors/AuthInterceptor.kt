package com.example.data.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(
    private val token: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder().also { builder: Request.Builder ->
                    builder.addHeader("Authorization", "Bearer $token")
                }
                .build()
        )

}

