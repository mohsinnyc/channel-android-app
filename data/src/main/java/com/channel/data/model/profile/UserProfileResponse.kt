package com.channel.data.model.profile

// User Profile Response Data Class

data class UserProfileResponse(
    val id: String,
    val username: String,
    val email: String,
    val profilePictureUrl: String?,
    val bio: String?
)
