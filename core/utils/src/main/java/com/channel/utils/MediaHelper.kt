package com.channel.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import javax.inject.Inject

class MediaHelper @Inject constructor() {

    fun createImageUri(context: Context): Uri {
        val imagesFolder = File(context.cacheDir, "images").apply { if (!exists()) mkdir() }
        val imageFile = File(imagesFolder, "${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", imageFile
        )
    }

    fun getImageMimeType(): String = "image/*"
}
