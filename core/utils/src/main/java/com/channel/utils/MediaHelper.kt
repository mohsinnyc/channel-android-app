package com.channel.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class MediaHelper @Inject constructor(@ApplicationContext private val context: Context) {
    private val imagesFolder: File = File(context.cacheDir, "images").apply {
        if (!exists()) mkdirs()
    }

    fun createImageUri(): Uri {
        val imageFile = File(imagesFolder, "${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", imageFile
        )
    }

    fun getImageMimeType(): String = "image/*"
}
