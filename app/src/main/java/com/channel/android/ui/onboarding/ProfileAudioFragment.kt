package com.channel.android.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.channel.android.databinding.FragmentComposeLayoutBinding
import com.channel.android.ui.onboarding.viewmodel.ProfileAudioViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileAudioFragment : Fragment() {
    private var _binding: FragmentComposeLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileAudioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup UI logic
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}