package com.channel.android.di

import com.channel.android.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("BASE_URL")
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL // ✅ Fetch BASE_URL from app's BuildConfig
    }
}
