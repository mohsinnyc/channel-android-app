package com.channel.data.repository

import com.channel.data.service.ProfileService
import com.channel.data.model.profile.UserProfileResponse
import com.channel.data.model.profile.OnboardingStatusResponse
import com.channel.data.utils.NetworkResult
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    retrofit: Retrofit
) {
    private val profileService: ProfileService = retrofit.create()

    suspend fun getProfile(): NetworkResult<UserProfileResponse> {
        return try {
            val response = profileService.getProfile()
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResult.Success(it)
                } ?: NetworkResult.Error(response.code(), "Empty Response")
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Throwable) {
            NetworkResult.Exception(e, "Failed to fetch profile")
        }
    }

    suspend fun getOnboardingStatus(): NetworkResult<OnboardingStatusResponse> {
        return try {
            val response = profileService.getOnboardingStatus()
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResult.Success(it)
                } ?: NetworkResult.Error(response.code(), "Empty Response")
            } else {
                NetworkResult.Error(response.code(), response.message())
            }
        } catch (e: Throwable) {
            NetworkResult.Exception(e, "Failed to fetch onboarding status")
        }
    }
}
