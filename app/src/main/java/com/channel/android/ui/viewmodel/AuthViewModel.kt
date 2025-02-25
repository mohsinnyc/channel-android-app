package com.channel.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.channel.data.repository.ProfileRepository
import com.channel.data.model.profile.OnboardingStatusResponse
import com.channel.data.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _onboardingStatus = MutableStateFlow<NetworkResult<OnboardingStatusResponse>>(NetworkResult.Idle)
    val onboardingStatus: StateFlow<NetworkResult<OnboardingStatusResponse>> get() = _onboardingStatus

    fun fetchOnboardingStatus() {
        viewModelScope.launch {
            _onboardingStatus.value = profileRepository.getOnboardingStatus()
        }
    }
}
