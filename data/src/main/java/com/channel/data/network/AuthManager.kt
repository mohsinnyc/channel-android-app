package com.channel.data.network

import com.channel.data.model.auth.AuthResponse
import com.channel.data.model.auth.LoginRequest
import com.channel.data.model.auth.SignUpRequest
import com.channel.data.model.auth.RefreshTokenRequest
import com.channel.data.repository.AuthRepository
import com.channel.data.session.AuthStateManager
import com.channel.data.storage.TokenManager
import com.channel.data.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val tokenManager: TokenManager,
    private val authStateManager: AuthStateManager,
    private val authRepository: AuthRepository
) {

    init {
        GlobalScope.launch(Dispatchers.IO) {
            val token = tokenManager.getToken()
            if (!token.isNullOrEmpty()) {
                authStateManager.setAuthenticated()
            } else {
                authStateManager.setLoggedOut()
            }
        }
    }

    suspend fun login(username: String, password: String): NetworkResult<AuthResponse> {
        return withContext(Dispatchers.IO) {
            val result = authRepository.login(LoginRequest(username, password))
            if (result is NetworkResult.Success) {
                saveAuthTokens(result.data)
                authStateManager.setAuthenticated()
            }
            result
        }
    }

    suspend fun signUp(username: String, password: String, confirmPassword: String): NetworkResult<AuthResponse> {
        return withContext(Dispatchers.IO) {
            val result = authRepository.signUp(SignUpRequest(username, password, confirmPassword))
            if (result is NetworkResult.Success) {
                saveAuthTokens(result.data)
                authStateManager.setAuthenticated()
            }
            result
        }
    }

    suspend fun refreshAccessToken(): NetworkResult<String> {
        authStateManager.setRefreshingToken()
        return withContext(Dispatchers.IO) {
            val refreshToken = tokenManager.getRefreshToken() ?: run {
                authStateManager.setLoggedOut()
                return@withContext NetworkResult.Error(401, "No refresh token available")
            }

            repeat(MAX_REFRESH_RETRIES) { attempt ->
                val result = authRepository.refreshToken(RefreshTokenRequest(refreshToken))
                if (result is NetworkResult.Success) {
                    val newAccessToken = result.data.accessToken
                    tokenManager.saveToken(newAccessToken)
                    tokenManager.saveRefreshToken(result.data.refreshToken)
                    authStateManager.setAuthenticated()
                    return@withContext NetworkResult.Success(newAccessToken)
                } else if (attempt == MAX_REFRESH_RETRIES - 1) {
                    authStateManager.setLoggedOut()
                    return@withContext NetworkResult.Error(401, "Max Refreshes Attempted")
                }
                delay(REFRESH_RETRY_DELAY)
            }
            NetworkResult.Error(500, "Unexpected token refresh failure")
        }
    }

    suspend fun logout(): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                tokenManager.clearTokens()
                authStateManager.setLoggedOut()
                NetworkResult.Success(Unit)
            } catch (e: Exception) {
                NetworkResult.Exception(e, "Logout failed")
            }
        }
    }

    private fun saveAuthTokens(authResponse: AuthResponse) {
        tokenManager.saveToken(authResponse.accessToken)
        tokenManager.saveRefreshToken(authResponse.refreshToken)
    }

    companion object {
        private const val MAX_REFRESH_RETRIES = 2
        private const val REFRESH_RETRY_DELAY = 2000L
    }
}
