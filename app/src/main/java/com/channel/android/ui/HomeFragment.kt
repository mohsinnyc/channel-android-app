package com.channel.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.channel.android.databinding.FragmentComposeLayoutBinding
import com.channel.android.ui.home.HomeScreen
import com.channel.data.auth.AuthManager
import com.channel.data.session.AuthStateManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentComposeLayoutBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var authManager: AuthManager
    @Inject lateinit var authStateManager: AuthStateManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            HomeScreen(
                authState = authStateManager.authState,
                onLogout = { logout() },
                onRefreshToken = { refreshToken() }
            )
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            authManager.logout()
        }
    }

    private fun refreshToken() {
        lifecycleScope.launch {
            authManager.refreshAccessToken()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
