package com.channel.android.ui.onboarding.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.channel.data.repository.ProfileRepository
import com.channel.data.utils.NetworkResult
import com.channel.utils.UriUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OnboardingPictureViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val uriUtils: UriUtils
) : ViewModel() {

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    private val _uploadState = MutableStateFlow<NetworkResult<Unit>>(NetworkResult.Idle)
    val uploadState: StateFlow<NetworkResult<Unit>> = _uploadState

    fun updateSelectedImage(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun onSubmit() {
        _selectedImageUri.value?.let { uri ->
            viewModelScope.launch {
                val imageFile = uriUtils.getFileFromUri(uri)
                if (imageFile != null && imageFile.exists()) {
                    uploadProfileImage(imageFile)
                }
            }
        }
    }

    private fun uploadProfileImage(imageFile: File) {
        viewModelScope.launch {
            _uploadState.value = NetworkResult.Loading
            val result = profileRepository.uploadProfileImage(imageFile)
            _uploadState.value = result
        }
    }
}
