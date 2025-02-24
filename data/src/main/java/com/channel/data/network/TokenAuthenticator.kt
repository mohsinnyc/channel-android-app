package com.channel.data.network

import com.channel.data.auth.AuthManager
import com.channel.data.storage.TokenManager
import com.channel.data.utils.NetworkResult
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val authManagerProvider: Provider<AuthManager> // Lazy inject to avoid circular dependency
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val authManager = authManagerProvider.get() // Get AuthManager instance lazily

        return runBlocking {
            when (val refreshResult = authManager.refreshAccessToken()) {
                is NetworkResult.Success -> retryRequestWithNewToken(response, refreshResult.data) // Use returned token
                else -> null
            }
        }
    }

    private fun retryRequestWithNewToken(response: Response, newToken: String): Request? {
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken") // Use returned token
            .build()
    }
}
