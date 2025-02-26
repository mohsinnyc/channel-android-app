package com.channel.data.service

import com.channel.data.model.profile.OnboardingStatusResponse
import com.channel.data.model.profile.UserProfileResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProfileService {

    @GET("/user/profile")
    suspend fun getProfile(): Response<UserProfileResponse>

    @GET("/user/onboarding-status")
    suspend fun getOnboardingStatus(): Response<OnboardingStatusResponse>
}
