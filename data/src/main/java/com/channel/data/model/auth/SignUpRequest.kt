package com.channel.data.model.auth

data class SignUpRequest(
    val username: String,
    val password: String,
    val confirmPassword: String
)
