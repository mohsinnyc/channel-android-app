package com.channel.android.ui.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.channel.data.model.profile.BioRequest
import com.channel.data.repository.ProfileRepository
import com.channel.data.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingBioViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _bioText = MutableStateFlow("")
    val bioText: StateFlow<String> = _bioText.asStateFlow()

    private val _uploadState = MutableStateFlow<NetworkResult<Unit>>(NetworkResult.Idle)
    val uploadState: StateFlow<NetworkResult<Unit>> = _uploadState

    /** Updates the current bio text */
    fun updateBioText(text: String) {
        _bioText.value = text
    }

    /** Submits the bio to the backend */
    fun onSubmit() {
        val bio = _bioText.value.trim()
        if (bio.isNotEmpty()) {
            viewModelScope.launch {
                _uploadState.value = NetworkResult.Loading
                val result = profileRepository.updateUserBio(BioRequest(bio))
                _uploadState.value = result
            }
        }
    }
}
