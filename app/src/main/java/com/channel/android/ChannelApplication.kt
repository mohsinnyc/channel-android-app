package com.channel.android

import android.app.Application
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.NavHostFragment
import com.channel.data.BuildConfig
import com.channel.data.auth.AuthManager
import com.channel.data.session.AuthState.*
import com.channel.data.session.AuthStateManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ChannelApplication : Application() {

    @Inject
    lateinit var sessionManager: AuthStateManager

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        CoroutineScope(Dispatchers.Main).launch {
            sessionManager.authState.collect { state ->
                when (state) {
                    LOGGED_OUT -> {
                        Timber.d("User logged out, navigating to Login Screen")
                        handleGlobalLogout()
                    }
                    AUTHENTICATED -> {
                        Timber.d("User authenticated")
                    }

                    REFRESHING_TOKEN -> {}
                }
            }
        }
    }

    private fun handleGlobalLogout() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }
}
