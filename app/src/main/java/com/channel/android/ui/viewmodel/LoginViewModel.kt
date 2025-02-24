package com.channel.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.channel.data.auth.AuthManager
import com.channel.data.models.auth.AuthResponse
import com.channel.data.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResult<AuthResponse>>(NetworkResult.Idle)
    val loginState: StateFlow<NetworkResult<AuthResponse>> = _loginState

    fun mockLogin() {
        viewModelScope.launch {
            _loginState.value = NetworkResult.Loading // ✅ Show loading state immediately
            Timber.d("Mock login: Loading state set")

            delay(2000) // ✅ Ensure UI sees the delay

            val mockResponse = AuthResponse(
                accessToken = "mockAccessToken123",
                refreshToken = "mockRefreshToken456"
            )

            authManager.mockLogin(mockResponse) // ✅ Store mock tokens
            _loginState.value = NetworkResult.Success(mockResponse) // ✅ Update UI state after delay

            Timber.d("Mock login successful: ${mockResponse.accessToken}")
        }
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = NetworkResult.Error(400, "Username and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _loginState.value = NetworkResult.Loading
            val result = authManager.login(username, password)
            _loginState.value = result
            logResult(result)
        }
    }

    private fun logResult(result: NetworkResult<AuthResponse>) {
        when (result) {
            is NetworkResult.Success -> Timber.d("Login successful: ${result.data}")
            is NetworkResult.Error -> Timber.e("Login error: ${result.message}")
            is NetworkResult.Exception -> Timber.e(result.e, "Login exception: ${result.errorMessage}")
            else -> Unit
        }
    }
}
