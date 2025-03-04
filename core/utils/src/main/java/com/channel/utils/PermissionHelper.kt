package com.channel.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionHelper @Inject constructor(@ApplicationContext private val context: Context) {

    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        object Permissions {
            const val CAMERA = Manifest.permission.CAMERA
            const val MICROPHONE = Manifest.permission.RECORD_AUDIO
            val STORAGE: String
                get() = when {
                    android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU ->
                        Manifest.permission.READ_MEDIA_IMAGES
                    android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q ->
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    else ->
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        }
    }
}
