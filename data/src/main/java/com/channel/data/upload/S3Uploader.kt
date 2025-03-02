package com.channel.data.upload

import com.channel.data.utils.NetworkResult
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Singleton
class S3Uploader @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val imageCompressor: ImageCompressor
) {
    suspend fun uploadToS3(signedUrl: String, file: File): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val compressedFile = imageCompressor.compressImage(file)
                val request = Request.Builder()
                    .url(signedUrl)
                    .put(compressedFile.asRequestBody("image/webp".toMediaTypeOrNull()))
                    .build()

                val response = okHttpClient.newCall(request).execute()
                response.use {
                    if (it.isSuccessful) {
                        NetworkResult.Success(Unit)
                    } else {
                        NetworkResult.Error(it.code, "S3 Upload Failed: ${it.message}")
                    }
                }
            } catch (e: Exception) {
                NetworkResult.Exception(e, "Exception during S3 upload: ${e.localizedMessage}")
            }
        }
    }
}
