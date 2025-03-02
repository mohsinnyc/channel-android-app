package com.channel.data.upload

import com.channel.data.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class S3Uploader @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    suspend fun uploadToS3(signedUrl: String, file: File): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(signedUrl)
                    .put(file.asRequestBody("image/jpeg".toMediaTypeOrNull())) // Adjust MIME type if needed
                    .build()

                val response = okHttpClient.newCall(request).execute()
                return@withContext if (response.isSuccessful) {
                    NetworkResult.Success(Unit)
                } else {
                    NetworkResult.Error(response.code, "S3 Upload Failed: ${response.message}")
                }
            } catch (e: Throwable) {
                NetworkResult.Exception(e, "Exception during S3 upload: ${e.localizedMessage}")
            }
        }
    }
}
