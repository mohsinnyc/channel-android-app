package com.channel.data.models.auth

data class RefreshTokenRequest(
    val refreshToken: String,
    val grantType: String = "refresh_token"
)
