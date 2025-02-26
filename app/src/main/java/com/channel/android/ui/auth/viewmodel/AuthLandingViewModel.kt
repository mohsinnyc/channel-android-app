package com.channel.android.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.channel.data.repository.ProfileRepository
import com.channel.data.model.profile.OnboardingStatusResponse
import com.channel.data.storage.PreferenceManager
import com.channel.data.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthLandingViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _onboardingStatus = MutableStateFlow<NetworkResult<OnboardingStatusResponse>>(NetworkResult.Idle)
    val onboardingStatus: StateFlow<NetworkResult<OnboardingStatusResponse>> get() = _onboardingStatus

    fun fetchOnboardingStatus() {
        viewModelScope.launch {
            val savedState = preferenceManager.getOnboardingState()
            if (savedState != null) {
                _onboardingStatus.value = NetworkResult.Success(OnboardingStatusResponse(savedState))
            } else {
                _onboardingStatus.value = profileRepository.getOnboardingStatus()
            }
        }
    }
}
