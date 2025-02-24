package com.channel.data.network

import com.channel.data.storage.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getToken()
        val requestBuilder = chain.request().newBuilder()

        // Add Authorization header if token exists
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        // Add Content-Type header
        requestBuilder.addHeader("Content-Type", "application/json")

        return chain.proceed(requestBuilder.build()) // Proceed with the request
    }
}
