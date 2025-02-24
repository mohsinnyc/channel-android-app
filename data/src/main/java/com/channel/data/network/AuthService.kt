package com.channel.data.network

import com.channel.data.models.auth.AuthResponse
import com.channel.data.models.auth.LoginRequest
import com.channel.data.models.auth.RefreshTokenRequest
import com.channel.data.models.auth.RefreshTokenResponse
import com.channel.data.models.auth.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>

    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}
