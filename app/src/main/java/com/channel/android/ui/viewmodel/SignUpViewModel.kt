package com.channel.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.channel.data.auth.AuthManager
import com.channel.data.models.auth.AuthResponse
import com.channel.data.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    private val _signUpState = MutableStateFlow<NetworkResult<AuthResponse>>(NetworkResult.Idle)
    val signUpState: StateFlow<NetworkResult<AuthResponse>> = _signUpState

    fun signUp(username: String, password: String, confirmPassword: String) {
        val validationError = validateInput(username, password, confirmPassword)
        if (validationError != null) {
            _signUpState.value = NetworkResult.Error(400, validationError)
            return
        }

        viewModelScope.launch {
            _signUpState.value = NetworkResult.Loading
            performSignUp(username, password, confirmPassword)
        }
    }

    private suspend fun performSignUp(username: String, password: String, confirmPassword: String) {
        try {
            val result = authManager.signUp(username, password, confirmPassword)
            handleSignUpResult(result)
        } catch (e: Exception) {
            _signUpState.value = NetworkResult.Exception(e, "An unexpected error occurred")
            Timber.e(e, "Exception during sign-up")
        }
    }

    private fun handleSignUpResult(result: NetworkResult<AuthResponse>) {
        when (result) {
            is NetworkResult.Success -> {
                _signUpState.value = result
                Timber.d("Sign-up successful, user logged in automatically")
            }
            is NetworkResult.Error -> {
                _signUpState.value = result
                Timber.e("Sign-up error: ${result.message}")
            }
            is NetworkResult.Exception -> {
                _signUpState.value = result
                Timber.e(result.e, "Sign-up exception: ${result.errorMessage}")
            }
            else -> Unit // Idle and Loading states are already managed
        }
    }

    private fun validateInput(username: String, password: String, confirmPassword: String): String? {
        return when {
            username.isBlank() -> "Username cannot be empty"
            password.isBlank() -> "Password cannot be empty"
            password.length < 6 -> "Password must be at least 6 characters"
            password != confirmPassword -> "Passwords do not match"
            else -> null
        }
    }
}
