package com.channel.data.auth

import com.channel.data.models.auth.AuthResponse
import com.channel.data.models.auth.LoginRequest
import com.channel.data.models.auth.SignUpRequest
import com.channel.data.models.auth.RefreshTokenRequest
import com.channel.data.network.AuthService
import com.channel.data.session.AuthStateManager
import com.channel.data.storage.TokenManager
import com.channel.data.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val tokenManager: TokenManager,
    private val authStateManager: AuthStateManager,
    retrofit: Retrofit
) {
    private val authService: AuthService = retrofit.create(AuthService::class.java)

    fun mockLogin(mockResponse: AuthResponse) {
        saveAuthTokens(mockResponse) // ✅ Store mock tokens
        authStateManager.setAuthenticated() // ✅ Simulate successful login
    }

    suspend fun login(username: String, password: String): NetworkResult<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authService.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        saveAuthTokens(authResponse)
                        authStateManager.setAuthenticated()
                        return@withContext NetworkResult.Success(authResponse)
                    }
                    return@withContext NetworkResult.Error(500, "Unexpected response format")
                }
                NetworkResult.Error(response.code(), response.errorBody()?.string())
            } catch (e: Exception) {
                NetworkResult.Exception(e, "Login request failed")
            }
        }
    }

    suspend fun signUp(username: String, password: String, confirmPassword: String): NetworkResult<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authService.signUp(SignUpRequest(username, password, confirmPassword))
                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        saveAuthTokens(authResponse)
                        authStateManager.setAuthenticated()
                        return@withContext NetworkResult.Success(authResponse)
                    }
                    return@withContext NetworkResult.Error(500, "Unexpected response format")
                }
                NetworkResult.Error(response.code(), response.errorBody()?.string())
            } catch (e: Exception) {
                NetworkResult.Exception(e, "Signup request failed")
            }
        }
    }

    suspend fun refreshAccessToken(): NetworkResult<String> {
        return withContext(Dispatchers.IO) {
            val refreshToken = tokenManager.getRefreshToken() ?: run {
                authStateManager.setLoggedOut()
                return@withContext NetworkResult.Error(401, "No refresh token available")
            }

            repeat(MAX_REFRESH_RETRIES) { attempt ->
                try {
                    authStateManager.setRefreshingToken()
                    val response = authService.refreshToken(RefreshTokenRequest(refreshToken))
                    if (response.isSuccessful) {
                        response.body()?.let { refreshResponse ->
                            val newAccessToken = refreshResponse.accessToken ?: return@let authStateManager.setLoggedOut()
                            val newRefreshToken = refreshResponse.refreshToken ?: refreshToken

                            tokenManager.saveToken(newAccessToken)
                            tokenManager.saveRefreshToken(newRefreshToken)

                            authStateManager.completeTokenRefresh(success = true)
                            return@withContext NetworkResult.Success(newAccessToken) // ✅ Return token directly
                        }
                    } else {
                        if (attempt == MAX_REFRESH_RETRIES - 1) {
                            authStateManager.setLoggedOut()
                            return@withContext NetworkResult.Error(response.code(), "Token refresh failed")
                        }
                        delay(REFRESH_RETRY_DELAY)
                    }
                } catch (e: Exception) {
                    if (attempt == MAX_REFRESH_RETRIES - 1) {
                        authStateManager.setLoggedOut()
                        return@withContext NetworkResult.Exception(e, "Token refresh request failed")
                    }
                    delay(REFRESH_RETRY_DELAY)
                }
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
        private const val MAX_REFRESH_RETRIES = 2 // ✅ Prevents infinite retry loops
        private const val REFRESH_RETRY_DELAY = 2000L // ✅ Wait 2 seconds before retrying
    }
}
