package com.channel.data.repository

import com.channel.data.model.auth.SignedUrlRequest
import com.channel.data.model.auth.SignedUrlResponse
import com.channel.data.model.profile.OnboardingStatusResponse
import com.channel.data.model.profile.UserProfileResponse
import com.channel.data.service.ProfileService
import com.channel.data.upload.S3Uploader
import com.channel.data.utils.NetworkResult
import com.channel.data.utils.NetworkResult.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

@Singleton
class ProfileRepository @Inject constructor(
    retrofit: Retrofit,
    private val s3Uploader: S3Uploader
) {
    private val profileService: ProfileService = retrofit.create()

    /** Fetch User Profile */
    suspend fun getProfile(): NetworkResult<UserProfileResponse> {
        return try {
            val response = profileService.getProfile()
            if (response.isSuccessful) {
                response.body()?.let {
                    Success(it)
                } ?: Error(response.code(), "Empty Response")
            } else {
                Error(response.code(), response.message())
            }
        } catch (e: Throwable) {
            Exception(e, "Failed to fetch profile")
        }
    }

    /** Fetch Onboarding Status */
    suspend fun getOnboardingStatus(): NetworkResult<OnboardingStatusResponse> {
        return try {
            val response = profileService.getOnboardingStatus()
            if (response.isSuccessful) {
                response.body()?.let {
                    Success(it)
                } ?: Error(response.code(), "Empty Response")
            } else {
                Error(response.code(), response.message())
            }
        } catch (e: Throwable) {
            Exception(e, "Failed to fetch onboarding status")
        }
    }

    /** Request a Pre-Signed URL for Image Upload */
    suspend fun getSignedUrl(file: File): NetworkResult<SignedUrlResponse> {
        return try {
            val response = profileService.getSignedUrl(
                SignedUrlRequest(file.name, "image/jpeg")
            )
            if (response.isSuccessful && response.body() != null) {
                Success(response.body()!!)
            } else {
                Error(response.code(), "Failed to get signed URL")
            }
        } catch (e: Throwable) {
            Exception(e, "Exception while getting signed URL")
        }
    }

    /** Upload Image to S3 */
    suspend fun uploadImageToS3(signedUrl: String, file: File): NetworkResult<Unit> {
        return s3Uploader.uploadToS3(signedUrl, file)
    }

    /** Notify Backend After Successful Upload */
    private suspend fun updateProfileImageUrl(fileUrl: String): NetworkResult<Unit> {
        return try {
            val response = profileService.updateProfileImageUrl(fileUrl)
            if (response.isSuccessful) {
                Success(Unit)
            } else {
                Error(response.code(), "Failed to update profile image URL")
            }
        } catch (e: Throwable) {
            Exception(e, "Exception while updating profile image URL")
        }
    }

    suspend fun uploadProfileImage(file: File): NetworkResult<Unit> {
        val signedUrlResult = getSignedUrl(file)
        if (signedUrlResult !is Success) {
            return when (signedUrlResult) {
                is Error -> Error(signedUrlResult.code, signedUrlResult.errorMessage)
                is Exception -> Exception(signedUrlResult.e, signedUrlResult.errorMessage)
                else -> Error(-1, "Unknown error occurred")
            }
        }
        val signedUrl = signedUrlResult.data.signedUrl
        val fileUrl = signedUrlResult.data.fileUrl
        val uploadResult = uploadImageToS3(signedUrl, file)
        if (uploadResult !is Success) {
            return when (uploadResult) {
                is Error -> Error(uploadResult.code, uploadResult.errorMessage)
                is Exception -> Exception(uploadResult.e, uploadResult.errorMessage)
                else -> Error(-1, "Unknown error occurred")
            }
        }
        return updateProfileImageUrl(fileUrl)
    }
}


