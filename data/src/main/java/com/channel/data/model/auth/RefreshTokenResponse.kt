package com.channel.data.model.auth

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String? // Some APIs may return a new refresh token
)
