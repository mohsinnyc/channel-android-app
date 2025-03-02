package com.channel.data.upload

import android.content.Context
import id.zelory.compressor.Compressor
import android.graphics.Bitmap
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageCompressor @Inject constructor(private val context: Context) {

    suspend fun compressImage(file: File, maxSize: Int = 1024, quality: Int = 80): File {
        return Compressor.compress(context, file) {
            resolution(maxSize, maxSize) // Resize
            quality(quality) // Set compression quality
            format(Bitmap.CompressFormat.JPEG) // Use WebP for smaller size
        }
    }
}
