package com.kevin.storyappdicoding.utils

import com.kevin.storyappdicoding.data.model.RequestHeaders
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class RequestInterceptor(private val requestHeaders: RequestHeaders) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder: Request.Builder = if (!requestHeaders.token.isNullOrBlank()) {
            original.newBuilder()
                .header("Authorization", "Bearer " + requestHeaders.token)
                .header("Accept", requestHeaders.language)
                .method(original.method, original.body)
        } else {
            original.newBuilder()
                .header("Accept", requestHeaders.language)
                .method(original.method, original.body)
        }
        val newRequest = builder.build()

        return chain.proceed(newRequest)
    }
}