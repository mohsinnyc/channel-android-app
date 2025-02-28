package com.channel.android.ui.onboarding.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.channel.data.repository.ProfileRepository
import com.channel.data.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileImageViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    private val _uploadState = MutableStateFlow<NetworkResult<Unit>>(NetworkResult.Idle)
    val uploadState: StateFlow<NetworkResult<Unit>> = _uploadState

    fun updateSelectedImage(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun uploadProfileImage() {
        val uri = selectedImageUri.value ?: return
        viewModelScope.launch {
            _uploadState.value = NetworkResult.Loading

            val imageFile = File(uri.path ?: return@launch)
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            _uploadState.value = profileRepository.setProfilePicture(imagePart)
        }
    }
}
