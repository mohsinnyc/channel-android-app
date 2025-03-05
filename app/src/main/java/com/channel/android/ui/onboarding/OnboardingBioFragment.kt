package com.channel.android.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.channel.android.databinding.FragmentComposeLayoutBinding
import com.channel.android.ui.onboarding.viewmodel.OnboardingBioViewModel
import com.channel.data.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingBioFragment : Fragment() {

    private var _binding: FragmentComposeLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingBioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            OnboardingBioScreen(
                bioText = viewModel.bioText.collectAsState().value,
                uploadState = viewModel.uploadState.collectAsState().value,
                onBioChange = { viewModel.updateBioText(it) },
                onSubmit = { viewModel.onSubmit() },
                onSkip = { /* Handle skipping the bio setup */ },
                onSuccess = {

                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
