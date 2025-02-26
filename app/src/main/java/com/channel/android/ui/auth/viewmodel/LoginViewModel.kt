package com.channel.android.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.channel.data.network.AuthManager
import com.channel.data.model.auth.AuthResponse
import com.channel.data.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResult<AuthResponse>>(NetworkResult.Idle)
    val loginState: StateFlow<NetworkResult<AuthResponse>> = _loginState

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
            is NetworkResult.Error -> Timber.e("Login error: ${result.errorMessage}")
            is NetworkResult.Exception -> Timber.e(result.e, "Login exception: ${result.errorMessage}")
            else -> Unit
        }
    }
}
