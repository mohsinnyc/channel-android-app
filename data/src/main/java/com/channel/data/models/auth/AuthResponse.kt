package com.channel.data.models.auth

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)
