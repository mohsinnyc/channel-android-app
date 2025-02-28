package com.channel.data.service

import com.channel.data.model.profile.OnboardingStatusResponse
import com.channel.data.model.profile.UserProfileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileService {

    @GET("/user/profile")
    suspend fun getProfile(): Response<UserProfileResponse>

    @GET("/user/onboarding-status")
    suspend fun getOnboardingStatus(): Response<OnboardingStatusResponse>

    @Multipart
    @POST("/user/profile-picture")
    suspend fun setProfilePicture(
        @Part image: MultipartBody.Part
    ): Response<Unit> // ✅ Returns empty response on success
}
