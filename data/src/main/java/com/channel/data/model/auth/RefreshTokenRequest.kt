package com.channel.data.model.auth

data class RefreshTokenRequest(
    val refreshToken: String,
    val grantType: String = "refresh_token"
)
