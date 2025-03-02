package com.channel.data.service

import com.channel.data.model.profile.OnboardingStatusResponse
import com.channel.data.model.profile.UserProfileResponse
import com.channel.data.model.auth.SignedUrlRequest
import com.channel.data.model.auth.SignedUrlResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileService {

    @GET("/user/profile")
    suspend fun getProfile(): Response<UserProfileResponse>

    @GET("/user/onboarding-status")
    suspend fun getOnboardingStatus(): Response<OnboardingStatusResponse>

    @POST("/user/profile-picture/signed-url")
    suspend fun getSignedUrl(
        @Body request: SignedUrlRequest
    ): Response<SignedUrlResponse>

    @POST("/user/profile-picture/upload-complete")
    suspend fun updateProfileImageUrl(
        @Body fileUrl: String
    ): Response<Unit>
}
