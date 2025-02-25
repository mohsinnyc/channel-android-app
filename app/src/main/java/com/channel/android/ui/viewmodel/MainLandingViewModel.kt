package com.channel.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.channel.data.session.AuthState
import com.channel.data.session.AuthStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainLandingViewModel @Inject constructor(
    private val authStateManager: AuthStateManager
) : ViewModel() {
    val authState: StateFlow<AuthState> = authStateManager.authState
}
