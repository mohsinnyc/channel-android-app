package com.channel.android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.channel.android.R
import com.channel.android.databinding.FragmentComposeLayoutBinding
import com.channel.android.ui.auth.viewmodel.AuthLandingViewModel
import com.channel.data.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthLandingFragment : Fragment() {
    private var _binding: FragmentComposeLayoutBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthLandingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComposeContent()
        authViewModel.fetchOnboardingStatus()
    }

    private fun setComposeContent() {
        binding.composeView.setContent {
            val onboardingState =
                authViewModel.onboardingStatus.collectAsState(initial = NetworkResult.Idle)
            AuthLandingScreen(
                onboardingStatus = onboardingState.value,
                onNavigateToOnboarding = {
                    setOnboardingNavGraph()
                },
                onNavigateToMainApp = {
                    //handle onboarded flow
                },
                onRefresh = {
                    authViewModel.fetchOnboardingStatus()
                }
            )
        }
    }

    private fun setOnboardingNavGraph() {
        findNavController().setGraph(R.navigation.nav_onboarding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
