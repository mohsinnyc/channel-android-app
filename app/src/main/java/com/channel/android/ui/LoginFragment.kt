package com.channel.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.channel.android.R
import com.channel.android.databinding.FragmentComposeLayoutBinding
import com.channel.android.ui.auth.LoginScreen
import com.channel.android.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentComposeLayoutBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            loginViewModel.loginState.collectLatest { state ->
                binding.composeView.setContent {
                    LoginScreen(
                        onNavigateToSignUp = {
                            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
                        },
                        loginState = state,
                        onLogin = { username, password ->
                            //loginViewModel.login(username, password)
                            loginViewModel.mockLogin()
                        },
                        onLoginSuccess = {
                            switchToHomeGraph()
                            switchToHomeGraph()
                        }
                    )
                }
            }
        }
    }

    private fun switchToHomeGraph() {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.home_nav_graph) // Dynamically switch to Home Graph
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
