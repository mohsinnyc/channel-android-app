package com.channel.data.session

import com.channel.data.storage.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStateManager @Inject constructor(
    private val tokenManager: TokenManager
) {

    private val _authState = MutableStateFlow(AuthState.LOGGED_OUT) // Default: Logged out
    val authState: StateFlow<AuthState> = _authState

    init {
        initialize() // Initialize auth state on app startup
    }

    private fun initialize() {
        val token = tokenManager.getToken()
        _authState.value = if (!token.isNullOrEmpty()) AuthState.AUTHENTICATED else AuthState.LOGGED_OUT
    }

    fun setAuthenticated() {
        _authState.value = AuthState.AUTHENTICATED
    }

    fun setLoggedOut() {
        _authState.value = AuthState.LOGGED_OUT
    }

    fun setRefreshingToken() {
        if (_authState.value != AuthState.REFRESHING_TOKEN) {
            _authState.value = AuthState.REFRESHING_TOKEN
        }
    }
}

enum class AuthState {
    AUTHENTICATED, LOGGED_OUT, REFRESHING_TOKEN
}
