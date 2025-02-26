package com.channel.data.repository

import com.channel.data.service.AuthService
import com.channel.data.model.auth.AuthResponse
import com.channel.data.model.auth.LoginRequest
import com.channel.data.model.auth.RefreshTokenRequest
import com.channel.data.model.auth.RefreshTokenResponse
import com.channel.data.model.auth.SignUpRequest
import com.channel.data.utils.NetworkResult
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    retrofit: Retrofit
) {
    private val authService: AuthService = retrofit.create()

    suspend fun refreshToken(request: RefreshTokenRequest): NetworkResult<RefreshTokenResponse> {
        return try {
            val response = authService.refreshToken(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResult.Success(it)
                } ?: NetworkResult.Error(response.code(), "Empty Response")
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Throwable) {
            NetworkResult.Exception(e, "Failed to refresh token")
        }
    }

    suspend fun signUp(request: SignUpRequest): NetworkResult<AuthResponse> {
        return try {
            val response = authService.signUp(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResult.Success(it)
                } ?: NetworkResult.Error(response.code(), "Empty Response")
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Throwable) {
            NetworkResult.Exception(e, "Failed to sign up")
        }
    }

    suspend fun login(request: LoginRequest): NetworkResult<AuthResponse> {
        return try {
            val response = authService.login(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResult.Success(it)
                } ?: NetworkResult.Error(response.code(), "Empty Response")
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Throwable) {
            NetworkResult.Exception(e, "Failed to log in")
        }
    }
}
