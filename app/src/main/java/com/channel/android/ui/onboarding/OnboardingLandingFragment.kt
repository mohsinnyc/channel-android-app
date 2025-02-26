package com.channel.android.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.channel.android.databinding.FragmentComposeLayoutBinding
import com.channel.android.ui.viewmodel.OnboardingLandingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingLandingFragment : Fragment() {

    private var _binding: FragmentComposeLayoutBinding? = null
    private val binding get() = _binding!!

    private val onboardingLandingViewModel: OnboardingLandingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation()
        onboardingLandingViewModel.determineNavigation()
    }

    private fun observeNavigation() {
        viewLifecycleOwner.lifecycleScope.launch {
            onboardingLandingViewModel.navigateToAction.collectLatest { actionId ->
                actionId?.let {
                    findNavController().navigate(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
