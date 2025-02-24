package com.channel.data.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStateManager @Inject constructor() {

    private val _authState = MutableStateFlow(AuthState.LOGGED_OUT) // Default: Logged out
    val authState: StateFlow<AuthState> = _authState

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

    fun completeTokenRefresh(success: Boolean) {
        if(success) {
            setAuthenticated()
        } else {
            setLoggedOut()
        }
    }
}

enum class AuthState {
    AUTHENTICATED, LOGGED_OUT, REFRESHING_TOKEN
}
