package com.channel.data.models.auth

data class SignUpRequest(
    val username: String,
    val password: String,
    val confirmPassword: String
)
