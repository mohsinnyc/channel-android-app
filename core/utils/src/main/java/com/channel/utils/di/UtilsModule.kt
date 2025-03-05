package com.channel.utils.di

import android.content.Context
import com.channel.utils.UriUtils
import com.channel.utils.MediaHelper
import com.channel.utils.PermissionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideMediaHelper(@ApplicationContext context: Context): MediaHelper {
        return MediaHelper(context)
    }

    @Provides
    @Singleton
    fun providePermissionHelper(@ApplicationContext context: Context): PermissionHelper {
        return PermissionHelper(context)
    }

    @Provides
    @Singleton
    fun provideUriUtils(@ApplicationContext context: Context): UriUtils {
        return UriUtils(context)
    }
}
