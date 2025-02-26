package com.channel.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.channel.data.model.profile.OnBoardingState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveOnboardingState(state: OnBoardingState) {
        prefs.edit().putString(KEY_ONBOARDING_STATE, state.name).apply()
    }

    fun getOnboardingState(): OnBoardingState? {
        val state = prefs.getString(KEY_ONBOARDING_STATE, null)
        return state?.let { OnBoardingState.valueOf(it) }
    }

    companion object {
        private const val PREFS_NAME = "channel_prefs"
        private const val KEY_ONBOARDING_STATE = "onboarding_state"
    }
}
