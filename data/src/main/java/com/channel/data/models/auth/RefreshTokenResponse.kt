package com.channel.data.models.auth

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String? // Some APIs may return a new refresh token
)
