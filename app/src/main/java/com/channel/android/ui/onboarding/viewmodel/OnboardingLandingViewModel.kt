package com.channel.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.channel.android.R
import com.channel.data.model.profile.OnBoardingState
import com.channel.data.storage.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingLandingViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _navigateToAction = MutableStateFlow<Int?>(null)
    val navigateToAction: StateFlow<Int?> = _navigateToAction

    fun determineNavigation() {
        viewModelScope.launch {
            val onboardingState = preferenceManager.getOnboardingState() ?: OnBoardingState.PROFILE_IMAGE
            _navigateToAction.value = when (onboardingState) {
                OnBoardingState.PROFILE_IMAGE -> R.id.action_onboardingLanding_to_profileImage
                OnBoardingState.PROFILE_BIO -> R.id.action_onboardingLanding_to_profileBio
                OnBoardingState.PROFILE_AUDIO -> R.id.action_onboardingLanding_to_profileAudio
                OnBoardingState.ON_BOARDING_COMPLETE -> null
            }
        }
    }
}
