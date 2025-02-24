package com.channel.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.channel.android.R
import com.channel.android.databinding.FragmentComposeLayoutBinding
import com.channel.android.ui.auth.SignUpScreen
import com.channel.android.ui.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentComposeLayoutBinding? = null
    private val binding get() = _binding!!
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            signUpViewModel.signUpState.collectLatest { state ->
                binding.composeView.setContent {
                    SignUpScreen(
                        onNavigateToLogin = {
                            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                        },
                        signUpState = state,
                        onSignUp = { username, password, confirmPassword ->
                            signUpViewModel.signUp(username, password, confirmPassword)
                        },
                        onSignUpSuccess = { navigateToHome() }
                    )
                }
            }
        }
    }

    private fun navigateToHome() {
        // Handle navigation to home screen after successful sign-up
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
