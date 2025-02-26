package com.channel.data.model.auth

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)
