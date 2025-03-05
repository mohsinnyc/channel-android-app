package com.channel.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class UriUtils @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun getFileFromUri(uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            when (uri.scheme) {
                "file" -> File(uri.path!!)
                "content" -> copyUriToTempFile(uri)
                else -> null
            }
        }
    }

    private fun copyUriToTempFile(uri: Uri): File? {
        val fileName = getFileName(uri) ?: return null
        val tempFile = File(context.cacheDir, fileName)

        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            tempFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileName(uri: Uri): String? {
        var name: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) name = it.getString(index)
            }
        }
        return name
    }
}
